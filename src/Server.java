import utility.CleintThread;
import utility.SQLmanager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;


public class Server {

    public static void run(int port){
        SQLmanager.connect();
        SQLmanager.getCollection();
        ServerSocketChannel serverChannel;
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress(port);
            serverChannel.bind(address);
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        System.out.println("канал открыт");
        while (true) {
            SocketChannel channel;
            try {
                do {
                    channel = serverChannel.accept();
                } while (Objects.isNull(channel));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("клиент подключен");
            CleintThread client = new CleintThread(channel);
            client.start();
        }
    }
}
