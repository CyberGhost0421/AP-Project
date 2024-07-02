package HTTPhandler;

import Controllers.UserController;
import Controllers.PostController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import org.json.*;
import utils.ExtractUserAuth;

import java.io.*;
import java.sql.SQLException;
import java.sql.Date;

public class PostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PostController postController = null;
        try {
            postController = new PostController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            // ip:port/tweets/
            case "POST":
                if (splittedPath.length == 3) {
                    if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {       //   ********************************
                        response = "permission-denied";
                        break;
                    }

                    InputStream requestBody = exchange.getRequestBody();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                    StringBuilder body = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        body.append(line);
                    }
                    requestBody.close();

                    // Process the user creation based on the request body
                    String newPost = body.toString();
                    JSONObject jsonObject = new JSONObject(newPost);
                    String postId = "";
                    try {
                        postId = postController.createPost(jsonObject.getString("writerId"), jsonObject.getString("ownerId"), jsonObject.getString("text"), toStringArray(jsonObject.getJSONArray("mediaPaths")), jsonObject.getInt("likes"), jsonObject.getInt("replies"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    response = postId;
                }
                else
                    response = "unknown request";
                break;
            case "GET":
                if (splittedPath.length == 2) {
                    try {
                        response = postController.getAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else if (splittedPath.length == 3) {
                    String postId = splittedPath[splittedPath.length - 1];
                    try {
                        response = postController.getPostById(postId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else
                    response = "alo!";
                break;
            case "DELETE":
                if (splittedPath.length == 2) {
                    try {
                        postController.deleteAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    response = "all tweets deleted";
                }
                else
                    response = "alo!";
                break;
            default:
                response = "unknown-request";
                break;
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public static String[] toStringArray(JSONArray array) {
        if(array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for(int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }
}