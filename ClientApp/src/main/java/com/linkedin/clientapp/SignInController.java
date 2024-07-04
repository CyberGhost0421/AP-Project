package com.linkedin.clientapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    @FXML
    private Button login_bt;

    @FXML
    private PasswordField password_tx;
    @FXML
    private TextField result_msg;
    @FXML
    private Hyperlink signup_bt;

    @FXML
    private TextField username_tf;

    public void initialize(URL url, ResourceBundle rb) {
//        sceneCleaner();
    }

    @FXML
    void onLoginClicked(ActionEvent event) throws Exception {

        if (username_tf.getText().isBlank() | password_tx.getText().isBlank()) {
            result_msg.setText("Please fill out all fields");

        } else {
            try {
                URL url = new URL("http://localhost:8080/sessions/" + username_tf.getText() + "/" + password_tx.getText());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");
                int responseCode = httpURLConnection.getResponseCode(); //temp

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputline;
                StringBuffer tempResponse = new StringBuffer();
                while ((inputline = reader.readLine()) != null) {
                    tempResponse.append(inputline);
                }
                reader.close();

                String response = tempResponse.toString();

                MainApplication.userToken = httpURLConnection.getHeaderField("JWT");

                if (response.equals("userID or userPassWord is incorrect")) {
                    result_msg.setText("Username or Password is incorrect");
                } else {
                    //change scene to user profile
                    MainApplication app = new MainApplication();
                    //get user info
                    url = new URL("http://localhost:8080/users/" + username_tf.getText());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    responseCode = httpURLConnection.getResponseCode(); //temp

                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputline2;
                    StringBuffer tempResponse2 = new StringBuffer();
                    while ((inputline2 = reader2.readLine()) != null) {
                        tempResponse2.append(inputline2);
                    }
                    reader2.close();
                    response = tempResponse2.toString();

                    JSONObject obj = new JSONObject(response);
                    User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumberType"),obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"),obj.getString("city"), new Date(obj.getLong("birthday")),obj.getString("socialLink"),new Date(obj.getLong("userCreatedAt")));
                    app.tokenFileWriter(obj.getString("token"));
                    MainApplication.userToken = app.tokenFileReader();
                    MainApplication.loggedInUser = user;
                    sceneCleaner();
                    app.changeCurrentScene("Profile");
                }
            } catch (ConnectException e) {
                result_msg.setText("connection failed");

            }
        }
    }

    public void sceneCleaner() {
        username_tf.setText("");
        password_tx.setText("");
        result_msg.setText("");
    }

    public void signUpButton(ActionEvent event) throws Exception {
        MainApplication app = new MainApplication();
        sceneCleaner();
        app.changeCurrentScene("SignUp");
    }


}
