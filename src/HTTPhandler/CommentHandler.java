package HTTPhandler;

import Controllers.CommentController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import utils.ExtractUserAuth;

import java.io.*;
import java.sql.SQLException;

public class CommentHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        CommentController commentController = null;
        try {
            commentController = new CommentController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET": //port:id/direct/person1/person2
                if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
                    response = "permission-denied";
                    break;
                }
//                if (splittedPath.length == 3) { //port:id/direct/person
//                    try {
//                        response = commentController.getNotify(splittedPath[2], 20);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
                try {
                    response = commentController.getComments(splittedPath[2]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "POST": // ip:port/direct + body
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();
                String newMessage = body.toString();
                JSONObject jsonObject = new JSONObject(newMessage);
                if (!jsonObject.getString("sender").equals(ExtractUserAuth.extract(exchange))) {
                    response = "permission-denied";
                    break;
                }
                response = "Done!";
                try {
                    commentController.addComment( jsonObject.getString("sender"), jsonObject.getString("text"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}