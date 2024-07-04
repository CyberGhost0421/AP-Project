package com.linkedin.clientapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

import static java.lang.System.in;

public class SearchController implements Initializable {

    @FXML
    private ListView list_lv;

    @FXML
    private Button search_bt;

    @FXML
    private TextField search_tf;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            searching(new ActionEvent());
        } catch (Exception e) {

        }
        list_lv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                String selected = list_lv.getSelectionModel().getSelectedItem().toString();
                System.out.println(selected);
                String[] selectedA = selected.split(" ");
                String ID = selectedA[0];
                try {
                    MainApplication.searchedUser = getUser(ID);
                    MainApplication app = new MainApplication();
                    app.changeCurrentScene("Profile");

                }catch (IOException e){
                    System.out.println("finding user failed");
                }
            }
        });

    }

    public void searching(ActionEvent event) throws IOException {
        String text = search_tf.getText();
        if (text.length() == 0 || text.charAt(0) != '#') {
            text.toLowerCase();


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

            for (String jsonUser : users) {
                JSONObject obj = new JSONObject(jsonUser);
                User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumberType"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"), obj.getString("city"), new Date(obj.getLong("birthday")), obj.getString("socialLink"), new Date(obj.getLong("userCreatedAt")));
                if (user.getId().toLowerCase().contains(text) || user.getFirstName().toLowerCase().contains(text) || user.getLastName().toLowerCase().contains(text)) {
                    usersIDArray.add(user.getId());
                    fullNameplusID.add(user.getId() + " | " + user.getFirstName() + " " + user.getLastName());
                }
            }
            for (String s : fullNameplusID) {
                System.out.println(s);
            }
            Collections.sort(fullNameplusID);
            list_lv.setItems(fullNameplusID);

        } else if (search_tf.getText().isBlank()) {

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

            for (String jsonUser : users) {
                JSONObject obj = new JSONObject(jsonUser);
                User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumberType"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"), obj.getString("city"), new Date(obj.getLong("birthday")), obj.getString("socialLink"), new Date(obj.getLong("userCreatedAt")));
                if (true) {
                    usersIDArray.add(user.getId());
                    fullNameplusID.add(user.getId() + " | " + user.getFirstName() + " " + user.getLastName());
                }
            }

            for (String s : fullNameplusID) {
                System.out.println(s);
            }

            Collections.sort(fullNameplusID);
            list_lv.setItems(fullNameplusID);

        }
    }

    public void setProfile_bt(ActionEvent event) throws Exception {

        MainApplication.searchedUser = null;

        MainApplication app = new MainApplication();
        app.changeCurrentScene("Profile");

    }

    public void logoutClicked(ActionEvent event) throws Exception {

        MainApplication app = new MainApplication();
        app.logedOut();
        app.changeCurrentScene("SignIn");

    }
    public static User getUser(String user_id) throws IOException {
        try {
            MainApplication m = new MainApplication();
            URL url = new URL("http://localhost:8080/users/" + user_id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader in1 = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline1;
            StringBuffer response2 = new StringBuffer();
            while ((inputline1 = in1.readLine()) != null) {
                response2.append(inputline1);
            }
            in.close();
            String response = response2.toString();
            JSONObject obj = new JSONObject(response);

            User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumberType"),obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"),obj.getString("city"), new Date(obj.getLong("birthday")),obj.getString("socialLink"),new Date(obj.getLong("userCreatedAt")));
            return user;
        }
        catch (ConnectException e) {
            return new User("","","","","", "", "", "", "", new Date(), "", new Date());
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

}
