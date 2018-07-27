package lucbui.rayscode.bot;

import lucbui.rayscode.evaluator.RayscodeEvaluator;
import lucbui.rayscode.lexer.RayscodeLexer;
import lucbui.rayscode.token.Rayscode;
import lucbui.rayscode.token.RayscodeFunctionMetadata;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Quick and dirty Listener Parser.
 */
public class RayscodeBotMessageListener extends ListenerAdapter {

    //A code cache, for each user.
    private static Map<User, StringBuilder> cacheToStore = new HashMap<>();
    //Paused listeners. We use a queue, so if multiple code is waiting for input, we unpause them in
    //request order.
    private static Queue<RayscodeEvaluator> pausedEvaluators = new LinkedList<>();

    public static final String PREFIX = "!";

    private enum Command {
        EVAL("eval", (evt, msg) -> evaluateCacheCode(evt, msg, false), (evt, msg) -> evaluateMessageCode(evt, msg, false),
                "Evaluates a piece of raysCode. If there are no arguments, the code in your cache is executed as raysCode."),
        DEBUG("debug", (evt, msg) -> evaluateCacheCode(evt, msg, true), (evt, msg) -> evaluateMessageCode(evt, msg, true),
                "Debugs a piece of raysCode. Every step of evaluation is DM'ed to you. If there are no arguments, the code in your cache is executed as raysCode"),
        STORE("store", RayscodeBotMessageListener::sendStoreToUser, RayscodeBotMessageListener::addToStore,
                "If there are no arguments, the contents of your cache are DM'ed to you. Otherwise, whatever you type is stored in your cache."),
        CLEAN("clean", RayscodeBotMessageListener::cleanCache, RayscodeBotMessageListener::cleanCache,
                "Deletes everything in your cache."),
        HELP("help", RayscodeBotMessageListener::displayHelpHelp, RayscodeBotMessageListener::displayCommandHelp,
                "Use !help followed by a command to get information on that command.");

        List<String> commands;
        BiConsumer<MessageReceivedEvent, Message> noAfter;
        BiConsumer<MessageReceivedEvent, Message> after;
        String help;

        Command(String commands, BiConsumer<MessageReceivedEvent, Message> noAfter, BiConsumer<MessageReceivedEvent, Message> after, String help){
            this.commands = Collections.singletonList(commands);
            this.noAfter = noAfter;
            this.after = after;
            this.help = help;
        }

        public List<String> getCommands() {
            return commands;
        }

        public BiConsumer<MessageReceivedEvent, Message> getNoAfter() {
            return noAfter;
        }

        public BiConsumer<MessageReceivedEvent, Message> getAfter() {
            return after;
        }

        public String getHelp() {
            return help;
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        Message message = event.getMessage();
        String rawMessage = message.getContentDisplay();
        MessageChannel channel = event.getChannel();

        //To check if I am the source of the message.
        User me = event.getJDA().getSelfUser();

        if(message.getMentionedUsers().stream()
                .anyMatch(user -> Objects.equals(user, me))) {
            channel.sendMessage("Hi! To use me, begin a message with !eval and follow it with your raysCode!\n\nThe raysCode specification can be found here: https://github.com/Justis-Lamanna/rayscode").queue();
        } else if(!Objects.equals(message.getAuthor(), me)){
            for (Command command : Command.values()) {
                if (command.getCommands().stream().anyMatch(i -> rawMessage.equals(PREFIX + i))) {
                    command.getNoAfter().accept(event, message);
                    break;
                } else if (command.getCommands().stream().anyMatch(i -> rawMessage.startsWith(PREFIX + i))) {
                    command.getAfter().accept(event, message);
                    break;
                }
            }
        } else if(!Objects.equals(message.getAuthor(), me) && !pausedEvaluators.isEmpty()){
            //If an evaluator is paused (waiting for input), we resume it now with the input message.
            RayscodeEvaluator eval = pausedEvaluators.remove();
            eval.setInputString(rawMessage);
            eval.setPaused(false);
            evaluate(eval, channel);
        } else if(rawMessage.startsWith(PREFIX)){
            //They're trying to use an unknown command
            channel.sendMessage("I don't recognize that command").queue();
        }
    }

    //"Compiles" a code string to some actual code.
    private static List<RayscodeFunctionMetadata> compile(String codeString) throws IOException{
        RayscodeLexer lexer = new RayscodeLexer(new StringReader(codeString));
        List<RayscodeFunctionMetadata> functionCode = new ArrayList<>();
        RayscodeFunctionMetadata code;
        while ((code = lexer.nextToken()) != null) {
            functionCode.add(code);
        }

        return functionCode;
    }

    //Evaluate some code, and posts the final results.
    private static void evaluate(RayscodeEvaluator eval, MessageChannel channel){
        Deque<BigInteger> endStack = eval.evaluate();
        if(eval.isPaused()){
            //Evaluator is waiting for input.
            pausedEvaluators.add(eval);
            channel.sendMessage("Waiting for input...").queue();
        } else {
            //Print stack if no output string is specified, otherwise print the output string.
            if(eval.getOutputString() == null || eval.getOutputString().isEmpty()) {
                channel.sendMessage("End Result: ```" + endStack.toString() + "```").queue();
            } else {
                channel.sendMessage("End Result: " + eval.getOutputString()).queue();
            }
        }
    }

    private static void evaluateCacheCode(MessageReceivedEvent event, Message message, boolean debug) {
        MessageChannel channel = event.getChannel();
        if(cacheToStore.containsKey(message.getAuthor())){
            String codeString = cacheToStore.get(message.getAuthor()).toString();
            try {
                RayscodeEvaluator eval = new RayscodeEvaluator(compile(codeString));
                eval.setDebug(debug);
                eval.setOutputMethod(str -> message.getAuthor().openPrivateChannel().complete().sendMessage(str).queue());
                evaluate(eval, channel);
            } catch (IOException ex){
                channel.sendMessage("Whoops I ran into an error. I've written it in the console.").queue();
                ex.printStackTrace();
            } catch (IllegalArgumentException ex){
                channel.sendMessage("Error encountered during parsing: " + ex.getLocalizedMessage() + ".").queue();
            } catch (Throwable ex){
                channel.sendMessage("Error encountered during parsing: " + ex.getLocalizedMessage() + ". I've written more info in the console.").queue();
                ex.printStackTrace();
            }
        } else {
            channel.sendMessage("You have no code stored.").queue();
        }
    }

    private static void evaluateMessageCode(MessageReceivedEvent event, Message message, boolean debug) {
        MessageChannel channel = event.getChannel();
        String codeString = message.getContentDisplay();
        String[] split = codeString.split("\\s+", 2);
        codeString = split[1];
        try {
            RayscodeEvaluator eval = new RayscodeEvaluator(compile(codeString));
            eval.setDebug(debug);
            eval.setOutputMethod(str -> message.getAuthor().openPrivateChannel().complete().sendMessage(str).queue());
            evaluate(eval, channel);
        } catch (IOException ex){
            channel.sendMessage("Whoops I ran into an error. I've written it in the console.").queue();
            ex.printStackTrace();
        } catch (IllegalArgumentException ex){
            channel.sendMessage("Error encountered during parsing: " + ex.getLocalizedMessage() + ".").queue();
        } catch (Throwable ex){
            channel.sendMessage("Error encountered during parsing: " + ex.getLocalizedMessage() + ". I've written more info in the console.").queue();
            ex.printStackTrace();
        }
    }

    private static void sendStoreToUser(MessageReceivedEvent event, Message message) {
        MessageChannel channel = event.getChannel();
        if(!cacheToStore.containsKey(message.getAuthor())){
            channel.sendMessage("You have no code stored.").queue();
        } else {
            MessageChannel dmChannel = event.isFromType(ChannelType.PRIVATE) ? channel : message.getAuthor().openPrivateChannel().complete();
            String storedValue = cacheToStore.get(message.getAuthor()).toString();
            for (int charIndex = 0; charIndex < storedValue.length(); charIndex += 2000){
                int charEndIndex = Math.min(charIndex + 2000, storedValue.length() - charIndex);
                dmChannel.sendMessage(storedValue.substring(charIndex, charEndIndex)).queue();
            }
            if(!event.isFromType(ChannelType.PRIVATE)) {
                channel.sendMessage("I DM'ed you the contents of your cache.").queue();
            }
        }
    }

    private static void addToStore(MessageReceivedEvent event, Message message) {
        MessageChannel channel = event.getChannel();
        String rawMessage = message.getContentDisplay().substring(6).trim();
        rawMessage = rawMessage.substring(6).trim();
        cacheToStore.computeIfAbsent(message.getAuthor(), u -> new StringBuilder()).append(rawMessage).append(" ");
        channel.sendMessage("I stored this code for later evaluation. You can add more to this code using the same command.").queue();
    }

    private static void cleanCache(MessageReceivedEvent event, Message message) {
        //Clean their store for re-use.
        if(cacheToStore.containsKey(message.getAuthor())){
            cacheToStore.remove(message.getAuthor());
            event.getChannel().sendMessage("Alright, I deleted everything you had stored.").queue();
        } else {
            event.getChannel().sendMessage("You don't have anything stored.").queue();
        }
    }

    private static void displayHelpHelp(MessageReceivedEvent event, Message message) {
        event.getChannel().sendMessage("Usage for help: ```" + Command.HELP.getHelp() + "```").queue();
    }

    private static void displayCommandHelp(MessageReceivedEvent event, Message message) {
        MessageChannel channel = event.getChannel();
        String rawMessage = message.getContentDisplay().split("\\s+", 2)[1];

        Optional<Command> match = Arrays.stream(Command.values())
                .filter(cmd -> cmd.getCommands().stream().anyMatch(i -> Objects.equals(i, rawMessage)))
                .findFirst();

        if(match.isPresent()){
            event.getChannel().sendMessage("Usage for " + match.get().toString().toLowerCase() + ":```" + match.get().getHelp() + "```").queue();
        } else {
            //Ugh this is awful
            try {
                RayscodeLexer lexer = new RayscodeLexer(new StringReader(rawMessage));
                RayscodeFunctionMetadata token = lexer.nextToken();
                event.getChannel().sendMessage("Usage for " + token.getFunction() + ":```" + token.getFunction().getHelp() + "```").queue();
            } catch (IOException ex) {
                channel.sendMessage("Whoops I ran into an error. I've written it in the console.").queue();
                ex.printStackTrace();
            } catch (Throwable ex) {
                event.getChannel().sendMessage("I don't have help for that command.").queue();
            }
        }
    }
}
