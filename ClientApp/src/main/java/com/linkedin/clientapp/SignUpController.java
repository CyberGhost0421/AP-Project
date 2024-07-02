package com.linkedin.clientapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URI;
import java.util.ResourceBundle;

public class SignUpController{

    @FXML
    private TextField city_tf;

    @FXML
    private ComboBox country_chb;

    @FXML
    private DatePicker dateofbirth_date;

    @FXML
    private TextField email_tf;

    @FXML
    private TextField firstname_tf;

    @FXML
    private TextField lastname_tf;

    @FXML
    private PasswordField password_tf;

    @FXML
    private ComboBox phonenumberType_chb;

    @FXML
    private TextField phonenumber_tf;

    @FXML
    private PasswordField repassword_tf;

    @FXML
    private Label resultMessage;

    @FXML
    private Hyperlink signin_bt;

    @FXML
    private Button signup_bt;

    @FXML
    private TextField sociallink_tf;

    @FXML
    private TextField username_tf;

//    @Override
//    public void initialize(URI url, ResourceBundle rb) {
//        ObservableList<String> list = FXCollections.observableArrayList("IRAN", "USA", "IRAQ", "TURKEY", "SPAIN", "UK", "DENMARK", "RUSSIA");
//        country_chb.setItems(list);
//    }

    public void setSignin_bt(ActionEvent event) throws Exception{
        MainApplication app = new MainApplication();
        sceneCleaner();
        app.changeCurrentScene("SignIn");
    }

    public void sceneCleaner() {
        firstname_tf.setText("");
        lastname_tf.setText("");
        phonenumber_tf.setText("");
//        phonenumberType_chb.setValue();
        city_tf.setText("");
        username_tf.setText("");
        password_tf.setText("");
        repassword_tf.setText("");
//        dateofbirth_date.setValue();
        sociallink_tf.setText("");
    }
    public static boolean isValidPass(String pass) {
        boolean cap = false, small = false;
        for (char c: pass.toCharArray()) {
            if (Character.isLowerCase(c))
                small = true;
            if (Character.isUpperCase(c))
                cap = true;
        }
        return cap && small && pass.length() >= 6;
    }

}
