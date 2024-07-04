package com.linkedin.clientapp;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

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
    String date = "";
    String Country = "";
    String PhoneNumberType = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sceneCleaner();
        ObservableList <String> list = FXCollections.observableArrayList("IRAN", "USA", "SWEDEN", "TURKEY", "SPAIN", "UK", "DENMARK", "RUSSIA","CANADA");
        country_chb.setItems(list);
        ObservableList <String> list2 = FXCollections.observableArrayList("Mobile","Work","Home");
        phonenumberType_chb.setItems(list2);
    }
    public void getDate(ActionEvent event) {
        date = dateofbirth_date.getValue().toString();
    }
    public void getPhoneNumberType(ActionEvent event) {
        PhoneNumberType = phonenumberType_chb.getValue().toString();
    }

    public void setSignup_bt(ActionEvent event) throws Exception {
        //checking all fields
        if (firstname_tf.getText().isBlank() ||lastname_tf.getText().isBlank() ||  phonenumber_tf.getText().isBlank() || phonenumberType_chb.getValue().toString().isBlank() || city_tf.getText().isBlank() || country_chb.getValue().toString().isBlank()  || username_tf.getText().isBlank() || email_tf.getText().isBlank() || password_tf.getText().isBlank()||repassword_tf.getText().isBlank()||dateofbirth_date.getValue().toString().isBlank()) {
            resultMessage.setText("please enter all fields");
        }
        else if (!email_tf.getText().isBlank() && !isValidEmailAddress(email_tf.getText())) {
            resultMessage.setText("Email is invalid");
        }
        else if (!isValidPass(password_tf.getText())) {
            resultMessage.setText("Password is invalid");
        }
        else if (!repassword_tf.getText().equals(password_tf.getText())) {
            resultMessage.setText("Passwords are not equal");
        }
        else {
            try {
                String response;
                {
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
                    response = response1.toString();
                }
                JSONArray jsonObject = new JSONArray(response);
                String[] users = toStringArray(jsonObject);
                boolean Email_existed = false;
                for (String t: users) {
                    JSONObject obj = new JSONObject(t);
                    User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"),obj.getString("phoneNumberType"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"),obj.getString("city"),null,null,null);
                    if (user.getEmail().equals(email_tf.getText()) && email_tf.getText().length() != 0)
                        Email_existed = true;
                    if (user.getPhoneNumber().equals(phonenumber_tf.getText()) && phonenumber_tf.getText().length() != 0)
                        Email_existed = true;
                }
                if (Email_existed) {
                    resultMessage.setText("Email or Phone existed");
                }
                else {
                    {
                        URL url = new URL("http://localhost:8080/users/" + username_tf.getText());
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
                        response = response1.toString();
                    }
                    if (!response.equals("No User")) {
                        resultMessage.setText("Username exist");
                    } else {
                        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                        Date date1 = format.parse(date);
                        User user = new User(username_tf.getText(), firstname_tf.getText(), lastname_tf.getText(), email_tf.getText(),PhoneNumberType ,phonenumber_tf.getText(), password_tf.getText(), Country, city_tf.getText(),date1, "",new Date());
                        //sending post request
                        URL url = new URL("http://localhost:8080/users");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();


                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(user);

                        byte[] postDataBytes = json.getBytes();

                        con.setRequestMethod("POST");
                        con.setDoOutput(true);
                        con.getOutputStream().write(postDataBytes);

                        Reader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        for (int c; (c = in.read()) > 0; )
                            sb.append((char) c);
                        response = sb.toString();

                        if (response.equals("this is done!")) {
                            sceneCleaner();
                            resultMessage.setText("Register completed");
                        } else
                            resultMessage.setText("Server error");
                    }
                }
            }
            catch (ConnectException e) {
                resultMessage.setText("connection failed");
            }
        }
    }

    public void selectCountry(ActionEvent event) {
        Country = country_chb.getSelectionModel().getSelectedItem().toString();
    }

    public void setSignin_bt(ActionEvent event) throws Exception {
        MainApplication app = new MainApplication();
        sceneCleaner();
        app.changeCurrentScene("SignIn");
    }

    public void sceneCleaner() {
        firstname_tf.setText("");
        lastname_tf.setText("");
        phonenumber_tf.setText("");
        phonenumberType_chb.getSelectionModel().clearSelection();
        city_tf.setText("");
        username_tf.setText("");
        password_tf.setText("");
        repassword_tf.setText("");
        dateofbirth_date.setValue(null);
        sociallink_tf.setText("");
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
        if(array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for(int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }

}
