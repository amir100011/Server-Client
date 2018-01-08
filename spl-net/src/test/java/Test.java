import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.Json.JsonMovie;
import bgu.spl181.net.impl.Json.JsonUser;
import bgu.spl181.net.impl.Users.RentedMovie;
import bgu.spl181.net.impl.Users.UserInfo;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.impl.generalImpls.BidiConnectionImple;
import bgu.spl181.net.srv.BaseServer;
import bgu.spl181.net.srv.Server;

import java.util.ArrayList;
import java.util.function.Supplier;

public class Test {

    public static void main(String[] args){
        String pathUsers = "/home/amir/Desktop/Spl/Server-Client-master/spl-net/example_Users.json";
        String pathMovies = "/home/amir/Desktop/Spl/Server-Client-master/spl-net/example_Movies.json";
        JsonUser JsonUsers = new JsonUser(pathUsers);
        JsonMovie jsonMovies = new JsonMovie(pathMovies);
        Supplier<MessageEncoderDecoder<String>> encdec = new Supplier<MessageEncoderDecoder<String>>() {
            @Override
            public MessageEncoderDecoder<String> get() {
                return new LineMessageEncoderDecoder();
            }
        };
        Supplier<BidiMessagingProtocol<String>> ProtocolSupplier = new Supplier<BidiMessagingProtocol<String>>() {
            @Override
            public BidiMessagingProtocol get() {
                return new BidiConnectionImple();
            }
        };
        Server<String> server = Server.threadPerClient(7777, ProtocolSupplier, encdec);
        ((BaseServer)server).setJson(JsonUsers, jsonMovies);
        server.serve();


    }

}
