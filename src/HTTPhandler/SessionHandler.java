package HTTPhandler;

import Controllers.UserController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.JwtAuth;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class SessionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        try {
            userController = new UserController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                String claimedUserId = splittedPath[splittedPath.length - 2];
                String userPass = splittedPath[splittedPath.length - 1];
                String result = null;
                try {
                    result = userController.getUserByIdAndPass(claimedUserId, userPass);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    response = "userID or userPassWord is incorrect";
                } else {
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("JWT", claimedUserId + "!" + JwtAuth.jws(claimedUserId));
                    response = "welcome";
                }
                break;
            default:
                break;
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}