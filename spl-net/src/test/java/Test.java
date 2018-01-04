import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.MessagingProtocol;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.BidiConnectionImpl;
import bgu.spl181.net.impl.echo.EchoClient;
import bgu.spl181.net.impl.echo.EchoProtocol;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.BaseServer;
import bgu.spl181.net.srv.BlockingConnectionHandler;
import bgu.spl181.net.srv.Server;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.function.Supplier;

public class Test {

    public static void main(String[] args){
        Supplier<MessageEncoderDecoder<String>> encdec = new Supplier<MessageEncoderDecoder<String>>() {
            @Override
            public MessageEncoderDecoder<String> get() {
                return new LineMessageEncoderDecoder();
            }
        };
        Supplier<BidiMessagingProtocol<String>> ProtocolSupplier = new Supplier<BidiMessagingProtocol<String>>() {
            @Override
            public BidiMessagingProtocol get() {
                return new BidiConnectionImpl();
            }
        };
        Server<String> server = Server.threadPerClient(7777, ProtocolSupplier, encdec);
        server.serve();
    }
}
