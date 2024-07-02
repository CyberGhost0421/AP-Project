package com.linkedin.clientapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.PrivateKey;

public class MainApplication extends Application {
    private static Stage currentStage;
    private static Scene currentScene;
    public static String userToken;
    public static User currentUser;


    @Override
    public void start(Stage stage) throws IOException {
        currentStage = stage;
        stage.setResizable(false);

        //set start scene to signin
        currentScene = new Scene((new FXMLLoader(MainApplication.class.getResource("SignIn.fxml"))).load());
        stage.setTitle("SignIn Panel");

        stage.setScene(currentScene);
        stage.show();
    }

    public void changeCurrentScene(String sceneName) throws IOException {
        switch (sceneName) {
            case "SignIn" :
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("SignIn.fxml"))).load()));
                break;
            case "SignUp" :
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("SignUp.fxml"))).load()));
                break;
            case "Profile" :
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("Profile.fxml"))).load()));
                break;
            case "Search" :
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("Search.fxml"))).load()));
                break;
            case "EditProfile" :
                currentStage.setScene(new Scene((new FXMLLoader(MainApplication.class.getResource("EditProfile.fxml"))).load()));
                break;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}