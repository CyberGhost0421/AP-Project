package com.linkedin.clientapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;

public class MainApplication extends Application {
    private static Stage currentStage;
    private static Scene currentScene;
    public static String userToken = "";
    public static User loggedInUser;
    public static User searchedUser;


    @Override
    public void start(Stage stage) throws IOException {

        if (!tokenFileReader().isBlank()) {
            userToken = tokenFileReader();
            System.out.println("loggedIn userToken : " + userToken);
        }

        currentStage = stage;

        if (userToken.isEmpty()) {
            currentScene = new Scene((new FXMLLoader(MainApplication.class.getResource("SignIn.fxml"))).load());

        } else if (getUserbyToken(userToken) != null) {
            loggedInUser = getUserbyToken(userToken);
            currentScene = new Scene((new FXMLLoader(MainApplication.class.getResource("Profile.fxml"))).load());
        } else {
            System.out.println("finding user error");
            return;
        }

        stage.setTitle("LinkedOut");
        stage.setResizable(false);

        stage.setScene(currentScene);
        stage.show();
    }

    public void logedOut() {
        searchedUser = null;
        loggedInUser = null;
        userToken = "";
        try {
            tokenFileWriter("");
        } catch (IOException e) {
            System.out.println("token File not found");
        }
    }

    public String tokenFileReader() throws IOException {
        int ch;
        String token = "";
        try {
            FileReader fr = new FileReader("userToken.txt");
            StringBuilder sb = new StringBuilder();
            while ((ch = fr.read()) != -1) {
                sb.append((char) ch);
            }
            token = sb.toString();
            fr.close();
        } catch (IOException fe) {
            System.out.println("file not found");
        }
        return token;
    }

    public void tokenFileWriter(String token) throws IOException {
        int ch;
        try {
            FileWriter fw = new FileWriter("userToken.txt");
            for (int i = 0; i < token.length(); i++)
                fw.write(token.charAt(i));
            fw.close();
        } catch (IOException fe) {
            System.out.println("file not found");
        }
    }


    public void changeCurrentScene(String sceneName) throws IOException {
        switch (sceneName) {
            case "SignIn":
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("SignIn.fxml"))).load()));
                break;
            case "SignUp":
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("SignUp.fxml"))).load()));
                break;
            case "Profile":
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("Profile.fxml"))).load()));
                break;
            case "Search":
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("Search.fxml"))).load()));
                break;
            case "EditProfile":
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("EditProfile.fxml"))).load()));
                break;
        }
    }

    public static User getUserbyToken(String user_token) throws IOException {
        try {
            URL url = new URL("http://localhost:8080/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response1 = new StringBuffer();
            while ((inputline = in.readLine()) != null) {
                response1.append(inputline);
            }
            in.close();
            String response = response1.toString();

            JSONArray jsonObject = new JSONArray(response);
            String[] users = toStringArray(jsonObject);

            ArrayList<String> usersIDArray = new ArrayList<>();
            ObservableList<String> fullNameplusID = FXCollections.observableArrayList();
            User tempUser = new User();
            for (String jsonUser : users) {
                JSONObject obj = new JSONObject(jsonUser);
                if (obj.getString("token").equals(user_token)) {
                    tempUser = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumberType"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"), obj.getString("city"), new Date(obj.getLong("birthday")), obj.getString("socialLink"), new Date(obj.getLong("userCreatedAt")));
                }
            }
            return tempUser;

        } catch (ConnectException e) {
            return new User("", "", "", "", "", "", "", "", "", new Date(), "", new Date());
        }
    }

    public static String[] toStringArray(JSONArray array) {
        if (array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }

    public static void main(String[] args) {
        launch();
    }
}