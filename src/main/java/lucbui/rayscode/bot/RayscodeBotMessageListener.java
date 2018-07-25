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

public class RayscodeBotMessageListener extends ListenerAdapter {

    private Map<User, StringBuilder> cacheToStore = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        Message message = event.getMessage();
        String rawMessage = message.getContentDisplay();
        MessageChannel channel = event.getChannel();

        User me = event.getJDA().getSelfUser();

        if(!event.isFromType(ChannelType.TEXT)){
            return;
        }
        //Check if the bot was pinged.
        if(message.getMentionedUsers().stream()
                .anyMatch(user -> Objects.equals(user, me))) {
            channel.sendMessage("Hi! To use me, begin a message with !eval and follow it with your raysCode!").queue();
        }
        else if(rawMessage.startsWith("!eval")) {
            String codeString;
            if(rawMessage.length() == 5){
                if(cacheToStore.containsKey(message.getAuthor())){
                    codeString = cacheToStore.get(message.getAuthor()).toString();
                } else {
                    channel.sendMessage("You have no code stored.").queue();
                    return;
                }
            } else {
                codeString = rawMessage.substring(5).trim();
            }
            try {
                RayscodeLexer lexer = new RayscodeLexer(new StringReader(codeString));
                List<RayscodeFunctionMetadata> functionCode = new ArrayList<>();
                RayscodeFunctionMetadata code;
                while ((code = lexer.nextToken()) != null) {
                    functionCode.add(code);
                }
                RayscodeEvaluator eval = new RayscodeEvaluator();
                Deque<BigInteger> endStack = eval.evaluate(functionCode);
                channel.sendMessage("End Result: ```" + endStack.toString() + "```").queue();
            } catch (IOException ex){
                channel.sendMessage("Whoops I ran into an error. I've written it in the console.").queue();
                ex.printStackTrace();
            } catch (Throwable ex){
                channel.sendMessage("Error encountered during parsing: " + ex.getLocalizedMessage() + ". I've written more info in the console.").queue();
                ex.printStackTrace();
            }
        }
        else if(rawMessage.startsWith("!store")) {
            if(rawMessage.length() == 6){
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
                rawMessage = rawMessage.substring(6).trim();
                cacheToStore.computeIfAbsent(message.getAuthor(), u -> new StringBuilder()).append(rawMessage).append(" ");
                channel.sendMessage("I stored this code for later evaluation. You can add more to this code using the same command.").queue();
            }
        }
        else if(rawMessage.equals("!clean")) {
            if(cacheToStore.containsKey(message.getAuthor())){
                cacheToStore.remove(message.getAuthor());
                channel.sendMessage("Alright, I deleted everything you had stored.").queue();
            } else {
                channel.sendMessage("You don't have anything stored.").queue();
            }
        }
        /*
        else if(rawMessage.startsWith("!help")) {
            String[] helpStringArray = rawMessage.split("//s+", 1);
            if(helpStringArray.length == 1){
                channel.sendMessage("To ask for help, use !help, followed by the command you want help for.").queue();
            } else {
                switch(helpStringArray[1]){
                    case "eval": channel.sendMessage("I can evaluate some rayscode for you. ")
                    default: channel.sendMessage("I can't help with that, unfortunately.").queue();
                }
            }
        } */else if(rawMessage.startsWith("!")){
            channel.sendMessage("I don't recognize that command").queue();
        }
    }
}
