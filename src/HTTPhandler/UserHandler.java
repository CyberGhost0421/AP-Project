package HTTPhandler;

import Controllers.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Date;

import org.json.JSONObject;

public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        EducationController educationController = null;
        SkillController skillController = null;
        try {
            userController = new UserController();
            educationController = new EducationController();
            skillController = new SkillController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 2) {
                    try {
                        response = userController.getUsers();
//                        System.out.println(response);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (splittedPath.length == 3) {
                    // Extract the user ID from the path
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = userController.getUserById(userId);
                        if (response == null) response = "No User";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (splittedPath.length == 4) {

                    String userId = splittedPath[2];
                    if (splittedPath[splittedPath.length - 1].equals("skills")) {
                        try {
                            response = skillController.getSkillsById(userId);
                            if (response == null) response = "No Skill";

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else if (splittedPath[splittedPath.length - 1].equals("educations")) {
                        try {
                            response = educationController.getEducationById(userId);
                            if (response.equals("No Education")) response = "No Education";
//                            System.out.println(response);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }


                } else if (splittedPath.length == 5) {
                    String userId = splittedPath[2];
                    String skillTitle = splittedPath[splittedPath.length - 1];
                    try {
                        response = skillController.getSkillById(userId, skillTitle);
                        if (response == null) response = "No Education";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case "POST":
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();

                if (splittedPath.length >= 4) {
                    if (splittedPath[3].equals("skills")) {
                        String newSkill = body.toString();
                        JSONObject jsonObject = new JSONObject(newSkill);
                        try {
                            skillController.createSkill(jsonObject.getString("id"), jsonObject.getString("skillTitle"), jsonObject.getString("skillDetail"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response = "this is done!";
                        break;
                    } else if (splittedPath[3].equals("educations")) {
                        String newEducation = body.toString();
                        JSONObject jsonObject = new JSONObject(newEducation);
                        try {
                            educationController.createEducation(jsonObject.getString("id"), jsonObject.getString("institute"), jsonObject.getString("study"), new Date(jsonObject.getLong("startedDate")), new Date(jsonObject.getLong("finishedDate")), jsonObject.getString("grade"), jsonObject.getString("activities"), jsonObject.getString("description"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response = "this is done!";
                        break;
                    }
                }

                // Process the user creation based on the request body
                String newUser = body.toString();
                JSONObject jsonObject = new JSONObject(newUser);
                try {
                    userController.createUser(jsonObject.getString("id"), jsonObject.getString("firstName"), jsonObject.getString("lastName"), jsonObject.getString("email"), jsonObject.getString("phoneNumberType"), jsonObject.getString("phoneNumber"), jsonObject.getString("password"), jsonObject.getString("country"), jsonObject.getString("city"), new Date(jsonObject.getLong("birthday")), jsonObject.getString("socialLink"), new Date(jsonObject.getLong("userCreatedAt")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Files.createDirectories(Paths.get("./Assets/" + jsonObject.getString("id")));
                response = "this is done!";
                break;

            case "PUT": //host/users/id/skills/title
                if (splittedPath.length == 3) {
                    requestBody = exchange.getRequestBody();
                    reader = new BufferedReader(new InputStreamReader(requestBody));
                    body = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        body.append(line);
                    }
                    requestBody.close();
                    newUser = body.toString();
                    jsonObject = new JSONObject(newUser);
                    try {

                        userController.updateUser(jsonObject.getString("id"), jsonObject.getString("firstName"), jsonObject.getString("lastName"), jsonObject.getString("email"), jsonObject.getString("phoneNumberType"), jsonObject.getString("phoneNumber"), jsonObject.getString("password"), jsonObject.getString("country"), jsonObject.getString("city"), new Date(jsonObject.getLong("birthday")), jsonObject.getString("socialLink"), new Date(jsonObject.getLong("userCreatedAt")));
                        response = "User Updated";

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (splittedPath.length == 4) {
                    requestBody = exchange.getRequestBody();
                    reader = new BufferedReader(new InputStreamReader(requestBody));
                    body = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        body.append(line);
                    }
                    requestBody.close();
                    jsonObject = new JSONObject(body.toString());

                    try {
                        educationController.updateEducation(jsonObject.getString("id"), jsonObject.getString("institute"), jsonObject.getString("study"), new Date(jsonObject.getLong("startedDate")), new Date(jsonObject.getLong("finishedDate")), jsonObject.getString("grade"), jsonObject.getString("activities"), jsonObject.getString("description"));
                        response = "Education Updated";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (splittedPath.length == 5) {

                    requestBody = exchange.getRequestBody();
                    reader = new BufferedReader(new InputStreamReader(requestBody));
                    body = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        body.append(line);
                    }
                    requestBody.close();
                    jsonObject = new JSONObject(body.toString());

                    try {
                        skillController.updateSkill(jsonObject.getString("id"), jsonObject.getString("skillTitle"), jsonObject.getString("skillDetail"), splittedPath[4]);
                        response = "Skill Updated";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
                break;
            case "DELETE": //host/users/id/skills/title
                if (splittedPath.length == 2) {
                    try {
                        userController.deleteUsers();
                        response = "All users deleted";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (splittedPath.length == 3) {
                    // Extract the user ID from the path
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        userController.deleteUser(userId);
                        response = "user deleted";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (splittedPath.length == 5) {
                    requestBody = exchange.getRequestBody();
                    reader = new BufferedReader(new InputStreamReader(requestBody));
                    body = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        body.append(line);
                    }
                    requestBody.close();

                    String userId = splittedPath[2];
                    String Title = splittedPath[4];
                    try {
                        skillController.deleteSkill(userId, Title);
                        response = "userSkill deleted";

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (splittedPath.length == 4) {
                    String userId = splittedPath[2];
                    try {
                        educationController.deleteEducation(userId);
                        response = "userEducation deleted";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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