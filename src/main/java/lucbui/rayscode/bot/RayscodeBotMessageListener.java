package lucbui.rayscode.bot;

import lucbui.rayscode.evaluator.RayscodeEvaluator;
import lucbui.rayscode.lexer.RayscodeLexer;
import lucbui.rayscode.token.RayscodeFunctionMetadata;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.*;

/**
 * Quick and dirty Listener Parser.
 */
public class RayscodeBotMessageListener extends ListenerAdapter {

    //A code cache, for each user.
    private Map<User, StringBuilder> cacheToStore = new HashMap<>();
    //Paused listeners. We use a queue, so if multiple code is waiting for input, we unpause them in
    //request order.
    private Queue<RayscodeEvaluator> pausedEvaluators = new LinkedList<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        Message message = event.getMessage();
        String rawMessage = message.getContentDisplay();
        MessageChannel channel = event.getChannel();

        //To check if I am the source of the message.
        User me = event.getJDA().getSelfUser();

        if(!event.isFromType(ChannelType.TEXT)){
            return; //Ignore DMs and other messages for now.
        }
        //Check if the bot was pinged.
        if(message.getMentionedUsers().stream()
                .anyMatch(user -> Objects.equals(user, me))) {
            channel.sendMessage("Hi! To use me, begin a message with !eval and follow it with your raysCode!").queue();
        } else if(rawMessage.startsWith("!eval")) {
            String codeString;
            if(rawMessage.length() == 5){
                //If the command is just "!eval", we evaluate whatever code is in the cache.
                if(cacheToStore.containsKey(message.getAuthor())){
                    codeString = cacheToStore.get(message.getAuthor()).toString();
                } else {
                    channel.sendMessage("You have no code stored.").queue();
                    return;
                }
            } else {
                //Otherwise, we evaluate the code following !eval.
                codeString = rawMessage.substring(5).trim();
            }
            try {
                evaluate(new RayscodeEvaluator(compile(codeString)), channel);
            } catch (IOException ex){
                channel.sendMessage("Whoops I ran into an error. I've written it in the console.").queue();
                ex.printStackTrace();
            } catch (Throwable ex){
                channel.sendMessage("Error encountered during parsing: " + ex.getLocalizedMessage() + ". I've written more info in the console.").queue();
                ex.printStackTrace();
            }
        } else if(rawMessage.startsWith("!store")) {
            //Store is used to bypass the 2000 character limit, for bigger functions.
            if(rawMessage.length() == 6){
                //If the command is just "!store", we DM the user whatever they have in their store.
                if(!cacheToStore.containsKey(message.getAuthor())){
                    channel.sendMessage("You have no code stored.").queue();
                } else {
                    PrivateChannel dmChannel = message.getAuthor().openPrivateChannel().complete();
                    String storedValue = cacheToStore.get(message.getAuthor()).toString();
                    for (int charIndex = 0; charIndex < storedValue.length(); charIndex += 2000){
                        int charEndIndex = Math.min(charIndex + 2000, storedValue.length() - charIndex);
                        dmChannel.sendMessage(storedValue.substring(charIndex, charEndIndex)).queue();
                    }
                    channel.sendMessage("I DM'ed you the contents of your cache.").queue();
                }
            } else {
                //Otherwise, we append whatever the user requests to store to their current store.
                rawMessage = rawMessage.substring(6).trim();
                cacheToStore.computeIfAbsent(message.getAuthor(), u -> new StringBuilder()).append(rawMessage).append(" ");
                channel.sendMessage("I stored this code for later evaluation. You can add more to this code using the same command.").queue();
            }
        } else if(rawMessage.equals("!clean")) {
            //Clean their store for re-use.
            if(cacheToStore.containsKey(message.getAuthor())){
                cacheToStore.remove(message.getAuthor());
                channel.sendMessage("Alright, I deleted everything you had stored.").queue();
            } else {
                channel.sendMessage("You don't have anything stored.").queue();
            }
        } else if(!Objects.equals(message.getAuthor(), me) && !pausedEvaluators.isEmpty()){
            //If an evaluator is paused (waiting for input), we resume it now with the input message.
            RayscodeEvaluator eval = pausedEvaluators.remove();
            eval.setInputString(rawMessage);
            eval.setPaused(false);
            evaluate(eval, channel);
        } else if(rawMessage.startsWith("!")){
            //They're trying to use an unknown command
            channel.sendMessage("I don't recognize that command").queue();
        }
    }

    //"Compiles" a code string to some actual code.
    private List<RayscodeFunctionMetadata> compile(String codeString) throws IOException{
        RayscodeLexer lexer = new RayscodeLexer(new StringReader(codeString));
        List<RayscodeFunctionMetadata> functionCode = new ArrayList<>();
        RayscodeFunctionMetadata code;
        while ((code = lexer.nextToken()) != null) {
            functionCode.add(code);
        }

        return functionCode;
    }

    //Evaluate some code, and posts the final results.
    private void evaluate(RayscodeEvaluator eval, MessageChannel channel){
        Deque<BigInteger> endStack = eval.evaluate();
        if(eval.isPaused()){
            //Evaluator is waiting for input.
            pausedEvaluators.add(eval);
            channel.sendMessage("Waiting for input...").queue();
        } else {
            //Print stack if no output string is specified, otherwise print the output string.
            if(eval.getOutputString() == null) {
                channel.sendMessage("End Result: ```" + endStack.toString() + "```").queue();
            } else {
                channel.sendMessage("End Result: " + eval.getOutputString()).queue();
            }
        }
    }
}
