package bot;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

// Mono is a reactive stream that sends only 0 or 1 element via the onNext signal
// that gets consumed right after the operation with a onComplete signal

public class BotMain {
    public static void main(String[] args) {
//        creating a bot by instancing a DiscordClient class
        DiscordClient client = DiscordClient.create("MTE5Mzg4Nzc1NzQzMzU2OTM3MA.Ge16nD.rtobI15hWrmZgk3TtTegS1f_HrWk5fSxNpWPFA");

//      creating an asynchronous process that returns nothing just to see the bot online
//        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> Mono.empty());

//        Creating an asynchronous process that, when the bot starts, it prints it's name in the console, by basically listening to the ready event and dispatching a message containing it's name.
//        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) ->
//              gateway.on(ReadyEvent.class, event -> Mono.fromRunnable(
//                   () -> {
//                              final User self = event.getSelf();
//                              System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
//                     })));


//        Creating an asynchronous process called login that will emit a 0 or 1 signal to the discord api
//        using the withGateway method to listen to new messages with the MessageCreateEvent handler
//        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> gateway.on(MessageCreateEvent.class, event -> {
////            Instancing a new Message object
//            Message message = event.getMessage();
//            String messageContent = message.getContent();
////            Getting it's content and checking for "!ping"
//            if(messageContent.equalsIgnoreCase("!ping")) {
////                if true, returning "Pong!"
//                return message.getChannel()
//                        .flatMap(channel -> channel.createMessage("Pong!"));
//            }
////          return nothing if message isn't !ping
//            return Mono.empty();
//        }));

        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> {
            Mono<Void> printOnLogin = gateway.on(ReadyEvent.class, event -> Mono.fromRunnable(() -> {
                final User self = event.getSelf();
                System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
            }))
                    .then();

            Mono<Void> handlePingCommand = gateway.on(MessageCreateEvent.class, event -> {
                Message message = event.getMessage();

                if(message.getContent().equalsIgnoreCase("!ping")) {
                    return message.getChannel()
                            .flatMap(channel -> channel.createMessage("pong!"));
                }
                return Mono.empty();
            }).then();

            return printOnLogin.and(handlePingCommand);
        });


        login.block();
    }
}
