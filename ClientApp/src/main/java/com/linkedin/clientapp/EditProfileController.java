package com.linkedin.clientapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import static java.lang.System.in;
import static java.lang.System.setOut;

public class EditProfileController implements Initializable {
    @FXML
    private Label DeleteAccount_bt;

    @FXML
    private CheckBox PV_checkbox;

    @FXML
    private Button Profile_bt;

    @FXML
    private TextField city_tf;

    @FXML
    private ChoiceBox country_cb;
    @FXML
    private Label resultMessage;
    @FXML
    private PasswordField currentPassword_tf;

    @FXML
    private DatePicker dateofbirth_tf;

    @FXML
    private TextField email2_tf;

    @FXML
    private TextField email_tf;

    @FXML
    private Button fileChooser_bt;

    @FXML
    private TextField firstname_tf;

    @FXML
    private TextField lastname_tf;

    @FXML
    private PasswordField newPassword_tf;

    @FXML
    private ChoiceBox phoneNumberType_cb;

    @FXML
    private TextField phoneNumber_tf;

    @FXML
    private PasswordField rePassword_tf;

    @FXML
    private Button save_bt;

    @FXML
    private TextField sociallink_tf;

    @FXML
    private TextField username2_tf;

    @FXML
    private TextField username_tf;
    private File AvatarFile = null;
    String dateofbirthselected ;
    String country ;
    String phoneNumberType;
    String newPass ;
    String socialLink;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            ObservableList<String> list = FXCollections.observableArrayList("IRAN", "USA", "SWEDEN", "TURKEY", "SPAIN", "UK", "DENMARK", "RUSSIA","CANADA");
            country_cb.setItems(list);
            ObservableList<String> list2 = FXCollections.observableArrayList("Mobile", "Work", "Home");
            phoneNumberType_cb.setItems(list2);

            country = MainApplication.loggedInUser.getCountry();
            dateofbirthselected = MainApplication.loggedInUser.getBirthday().toString();
            phoneNumberType = MainApplication.loggedInUser.getPhoneNumberType();
            newPass = MainApplication.loggedInUser.getPassword();


            country_cb.setValue(country);
            phoneNumberType_cb.setValue(phoneNumberType);

            User thisuser = getUser(MainApplication.loggedInUser.getId());

            username_tf.setText(thisuser.getId());
            username2_tf.setText(thisuser.getId());
            firstname_tf.setText(thisuser.getFirstName());
            lastname_tf.setText(thisuser.getLastName());
            phoneNumber_tf.setText(thisuser.getPhoneNumber());
            city_tf.setText(thisuser.getCity());
            email_tf.setText(thisuser.getEmail());
            email2_tf.setText(thisuser.getEmail());
            sociallink_tf.setText(thisuser.getSocialLink());
//            PV_checkbox

        } catch (IOException exception) {

        }

    }

    public void getDate(ActionEvent event) {
        dateofbirthselected = dateofbirth_tf.getValue().toString();
    }

    public void getPhoneNumberType(ActionEvent event) {
        phoneNumberType = phoneNumberType_cb.getValue().toString();
    }

    public void selectCountry(ActionEvent event) {
        country = country_cb.getSelectionModel().getSelectedItem().toString();
    }

    public void SaveChangePressed(ActionEvent event) throws Exception {
        {

            // main app user
            if (firstname_tf.getText().isBlank() || lastname_tf.getText().isBlank() || phoneNumber_tf.getText().isBlank() || phoneNumberType_cb.getValue().toString().isBlank() || city_tf.getText().isBlank() || country_cb.getValue().toString().isBlank() || email_tf.getText().isBlank()||email2_tf.getText().isBlank()) {
                resultMessage.setText("please enter all fields");
                return;
            }
            if (!email2_tf.getText().equals(MainApplication.loggedInUser.getEmail()) &&!isValidEmailAddress(email2_tf.getText())) {
                resultMessage.setText("Email is invalid");
                return;
            }
            if (!newPassword_tf.getText().isBlank() || !rePassword_tf.getText().isBlank() || !currentPassword_tf.getText().isBlank()) {
                if (!currentPassword_tf.getText().equals(MainApplication.loggedInUser.getPassword())) {
                    resultMessage.setText("CurrentPass is wrong");
                    return;
                } else if (!isValidPass(newPassword_tf.getText())) {
                    resultMessage.setText("NewPass Entered is invalid");
                    return;
                } else if (!rePassword_tf.getText().equals(newPassword_tf.getText())) {
                    resultMessage.setText("new Passwords are not equal");
                    return;
                }
            }

            {

                if (!newPassword_tf.getText().isBlank()) {
                    newPass = newPassword_tf.getText();
                }

                if (!sociallink_tf.getText().isBlank()){
                    socialLink = sociallink_tf.getText();
                }else {
                    socialLink = "";
                }


                try {
                    String response;

                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                    Date date1 = format.parse(dateofbirthselected);
                    System.out.println(dateofbirthselected);

                    User user = new User(MainApplication.loggedInUser.getId(), firstname_tf.getText(), lastname_tf.getText(), email_tf.getText(), phoneNumberType, phoneNumber_tf.getText(), newPass, country, city_tf.getText(), date1, socialLink, MainApplication.loggedInUser.getUserCreatedAt());


                    URL url = new URL("http://localhost:8080/users/" + username_tf.getText());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(user);
                    System.out.println(json);
                    byte[] postDataBytes = json.getBytes();
                    con.setRequestMethod("PUT");
                    con.setDoOutput(true);
                    con.getOutputStream().write(postDataBytes);
                    Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = in.read()) > 0; )
                        sb.append((char) c);
                    response = sb.toString();

                    if (response.equals("User Updated")) {
//                        sceneCleaner();
                        resultMessage.setText("User Updated");

                        MainApplication.loggedInUser = getUser(MainApplication.loggedInUser.getId());
                        MainApplication app = new MainApplication();
                        app.changeCurrentScene("EditProfile");

                    } else {
                        resultMessage.setText("Server error");
                    }
//
                } catch (ConnectException e) {
                    resultMessage.setText("connection failed");
                }
            }


            if (AvatarFile != null) {
                try {
                    String response;
                    URL url = new URL("http://localhost:8080/media/" + MainApplication.loggedInUser.getId() + "/Avatar/png");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestProperty("JWT", MainApplication.userToken);

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    OutputStream outputStream = con.getOutputStream();
                    Files.copy(AvatarFile.toPath(), outputStream);
                    outputStream.close();

                    Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = in.read()) > 0; )
                        sb.append((char) c);
                    response = sb.toString();
                    System.out.println(response);

                } catch (ConnectException e) {
                    System.out.println("Connection failed");
                }
            }
        }

    }

    public void setProfile_bt(ActionEvent event) throws Exception {

        MainApplication.searchedUser = null;

        MainApplication app = new MainApplication();
        app.changeCurrentScene("Profile");

    }

    public void logoutClicked(ActionEvent event) throws Exception {

        MainApplication.loggedInUser = null;
        MainApplication.searchedUser = null;

        MainApplication app = new MainApplication();
        app.changeCurrentScene("SignIn");

    }

//    public void AvatarFileChooser(ActionEvent event) throws Exception {
//        FileChooser fc = new FileChooser();
//        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.png"));
//        File f = fc.showOpenDialog(null);
//
//        if (f != null) {
//            AvatarFile = f;
//            Avatar_label.setText("Selected File::" + f.getAbsolutePath());
//        }
//    }

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

            User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumberType"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"), obj.getString("city"), new Date(obj.getLong("birthday")), obj.getString("socialLink"), new Date(obj.getLong("userCreatedAt")));
            return user;
        } catch (ConnectException e) {
            return new User("", "", "", "", "", "", "", "", "", new Date(), "", new Date());
        }
    }
    public void AvatarFileChooser(ActionEvent event) throws Exception {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.png"));
        File f = fc.showOpenDialog(null);

        if (f != null) {
            AvatarFile = f;
//            Avatar_label.setText("Selected File::" + f.getAbsolutePath());
        }
    }
    public static boolean isValidPass(String pass) {
        boolean cap = false, small = false;
        for (char c : pass.toCharArray()) {
            if (Character.isLowerCase(c))
                small = true;
            if (Character.isUpperCase(c))
                cap = true;
        }
        return cap && small && pass.length() >= 6;
    }


    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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
