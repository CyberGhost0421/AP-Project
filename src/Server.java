import HttpHandler.*;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {
    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get("./Assets/"));
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
//            server.createContext("/bios", new BioHandler());
            server.createContext("/users", new UserHandler());
            server.createContext("/sessions", new SessionHandler());
            server.createContext("/posts", new PostHandler());
            server.createContext("/direct", new MessageHandler());
            server.createContext("/media", new MediaHandler());
            server.createContext("/like", new LikeHandler());
            server.createContext("/follows", new FollowHandler());
            server.createContext("/comments", new CommentHandler());


            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}