package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.Json.JsonMovie;
import bgu.spl181.net.impl.Json.JsonUser;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.impl.generalImpls.BidiConnectionImple;
import bgu.spl181.net.srv.BaseServer;
import bgu.spl181.net.srv.Reactor;
import bgu.spl181.net.srv.Server;

import java.util.function.Supplier;

public class TPCMain {
    public static void main(String[] args){
        Integer port = Integer.parseInt(args[0]);
        String path = System.getProperty("user.dir");
        String pathUsers = path + "/Database/Users.json";
        String pathMovies = path + "/Database/Movies.json";
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
        Server<String> server = Server.threadPerClient(port, ProtocolSupplier, encdec);
        ((BaseServer)server).setJson(JsonUsers, jsonMovies);
        server.serve();


    }
}
