package com.linkedin.clientapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.transform.Shear;
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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
    private TextArea activities_tf;

    @FXML
    private TextField city_tf;

    @FXML
    private ChoiceBox country_cb;

    @FXML
    private PasswordField currentPassword_tf;

    @FXML
    private DatePicker dateofbirth_tf;

    @FXML
    private FontAwesomeIcon delete_bt;

    @FXML
    private TextArea description_tf;

    @FXML
    private TextArea detail_tf;

    @FXML
    private FontAwesomeIcon edit_bt;

    @FXML
    private TextField email2_tf;

    @FXML
    private TextField email_tf;

    @FXML
    private Button fileChooser_bt;

    @FXML
    private TextField finishedDate_tf;

    @FXML
    private TextField firstname_tf;

    @FXML
    private TextField grade_tf;

    @FXML
    private TextField institute_tf;

    @FXML
    private TextField lastname_tf;

    @FXML
    private PasswordField newPassword_tf;

    @FXML
    private ChoiceBox phoneNumberType_cb;

    @FXML
    private TextField phoneNumber_tf;

    @FXML
    private FontAwesomeIcon plus_bt;

    @FXML
    private PasswordField rePassword_tf;

    @FXML
    private Label resultMessage;

    @FXML
    private Button save_bt;

    @FXML
    private ListView skillListView;

    @FXML
    private Button skillsave_bt;

    @FXML
    private TextField sociallink_tf;

    @FXML
    private TextField startedDate_tf;

    @FXML
    private TextField study_tf;

    @FXML
    private TextField title_tf;

    @FXML
    private TextField username2_tf;

    @FXML
    private TextField username_tf;
    private File AvatarFile = null;

    String country;
    String phoneNumberType;
    String newPass;
    String socialLink;
    String selectedTitle;
    LocalDate dateofbirthselected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ObservableList<String> list = FXCollections.observableArrayList("IRAN", "USA", "SWEDEN", "TURKEY", "SPAIN", "UK", "DENMARK", "RUSSIA", "CANADA");
            country_cb.setItems(list);
            ObservableList<String> list2 = FXCollections.observableArrayList("Mobile", "Work", "Home");
            phoneNumberType_cb.setItems(list2);

            Instant instant = MainApplication.loggedInUser.getBirthday().toInstant();
            // Step 2: Convert java.time.Instant to java.time.ZoneId
            ZoneId zoneId = ZoneId.systemDefault();
            // Step 3: Convert java.time.Instant to java.time.LocalDate
            LocalDate localDate = instant.atZone(zoneId).toLocalDate();
            dateofbirth_tf.setValue(localDate);
            dateofbirthselected = localDate;


            country = MainApplication.loggedInUser.getCountry();
            phoneNumberType = MainApplication.loggedInUser.getPhoneNumberType();
            newPass = MainApplication.loggedInUser.getPassword();


            country_cb.setValue(country);
            phoneNumberType_cb.setValue(phoneNumberType);

            User thisuser = getUser(MainApplication.loggedInUser.getId());

            title_tf.setVisible(false);
            detail_tf.setVisible(false);
            skillsave_bt.setVisible(false);

            try {
                skillListView.setItems(skillsGetter(MainApplication.loggedInUser.getId()));
                skillListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        selectedTitle = skillListView.getSelectionModel().getSelectedItem().toString();
                        String id = MainApplication.loggedInUser.getId();
                        try {
                            Skill skillSelected = skillGetter(id, selectedTitle);
                            detail_tf.setText(skillSelected.getSkillDetail());
                            title_tf.setText(selectedTitle);
                            title_tf.setVisible(true);
                            detail_tf.setVisible(true);
                            skillsave_bt.setVisible(false);

                        } catch (IOException e) {
                            System.out.println("finding user Skill failed");
                        }
                    }
                });
            } catch (Exception e) {

            }

            username_tf.setText(thisuser.getId());
            username2_tf.setText(thisuser.getId());
            firstname_tf.setText(thisuser.getFirstName());
            lastname_tf.setText(thisuser.getLastName());
            phoneNumber_tf.setText(thisuser.getPhoneNumber());
            city_tf.setText(thisuser.getCity());
            email_tf.setText(thisuser.getEmail());
            email2_tf.setText(thisuser.getEmail());
            sociallink_tf.setText(thisuser.getSocialLink());

            setEducationToEditS();


//            PV_checkbox

        } catch (IOException exception) {

        }

    }

    public void getDate(ActionEvent event) {
        dateofbirthselected = dateofbirth_tf.getValue();
        System.out.println("get date "+dateofbirthselected);
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
            if (firstname_tf.getText().isBlank() || lastname_tf.getText().isBlank() || phoneNumber_tf.getText().isBlank() || phoneNumberType_cb.getValue().toString().isBlank() || city_tf.getText().isBlank() || country_cb.getValue().toString().isBlank() || email_tf.getText().isBlank() || email2_tf.getText().isBlank()) {
                resultMessage.setText("please enter all fields");
                return;
            }
            if (study_tf.getText().isBlank() || institute_tf.getText().isBlank() || grade_tf.getText().isBlank() || startedDate_tf.getText().isBlank() || finishedDate_tf.getText().isBlank() || activities_tf.getText().isBlank() || description_tf.getText().isBlank()) {
                resultMessage.setText("please enter all fields");
                return;
            }
            if (!email2_tf.getText().equals(MainApplication.loggedInUser.getEmail()) && !isValidEmailAddress(email2_tf.getText())) {
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

                if (!sociallink_tf.getText().isBlank()) {
                    socialLink = sociallink_tf.getText();
                } else {
                    socialLink = "";
                }


                try {
                    String response;

                    Date utilDate = Date.from(dateofbirthselected.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    System.out.println(utilDate);

                    User user = new User(MainApplication.loggedInUser.getId(), firstname_tf.getText(), lastname_tf.getText(), email_tf.getText(), phoneNumberType, phoneNumber_tf.getText(), newPass, country, city_tf.getText(), utilDate, socialLink, MainApplication.loggedInUser.getUserCreatedAt());


                    URL url = new URL("http://localhost:8080/users/" + username_tf.getText());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(user);
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

            saveEducation(MainApplication.loggedInUser.getId());
            setEducationToEditS();

        }

    }

    public java.sql.Date dateFormatter(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
            // Parse the string date to a java.util.Date object
            Date utilDate = sdf.parse(date);

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            sdf.setLenient(false);
            // Print the result
            return sqlDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setEdit_bt(MouseEvent event) throws IOException {
        if (selectedTitle == null) {
            return;
        }
        Skill skill = skillGetter(MainApplication.loggedInUser.getId(), selectedTitle);
        title_tf.setVisible(true);
        title_tf.setText(selectedTitle);
        detail_tf.setVisible(true);
        detail_tf.setText(skill.getSkillDetail());
        skillsave_bt.setVisible(true);
        skillsave_bt.setText("Edit skill");

        skillsave_bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (title_tf.getText().isBlank() || detail_tf.getText().isBlank()) {
                    resultMessage.setText("PLease fill out all fields");
                    return;
                }
                try {
                    editSkill(skill.getId(), selectedTitle);
                    skillListView.setItems(skillsGetter(MainApplication.loggedInUser.getId()));
                } catch (Exception e) {
                    System.out.println("Editing skill error");
                }
            }
        });

    }

    public void setDelete_bt(MouseEvent event) throws IOException {
        if (selectedTitle == null) {
            return;
        }
        Skill skill = skillGetter(MainApplication.loggedInUser.getId(), selectedTitle);

        title_tf.setVisible(true);
        detail_tf.setVisible(true);
        skillsave_bt.setVisible(false);

        try {
            deleteSkill(skill.getId(), selectedTitle);
        } catch (Exception e) {
            System.out.println("removing skill error");
        }
        skillListView.setItems(skillsGetter(MainApplication.loggedInUser.getId()));


    }

    public void setPlus_bt(MouseEvent event) throws IOException {

        title_tf.setVisible(true);
        title_tf.setText("");
        detail_tf.setVisible(true);
        detail_tf.setText("");
        skillsave_bt.setVisible(true);
        skillsave_bt.setText("Add to skills");

        skillsave_bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (title_tf.getText().isBlank() || detail_tf.getText().isBlank()) {
                    resultMessage.setText("PLease fill out all fields");
                    return;
                }
                try {
                    addtoSkills(new ActionEvent());
                    skillListView.setItems(skillsGetter(MainApplication.loggedInUser.getId()));
                } catch (Exception e) {
                    System.out.println("adding skill error");
                }
            }
        });

    }

    public void addtoSkills(ActionEvent event) throws IOException {
        String response;


        Skill skill = new Skill(MainApplication.loggedInUser.getId(), title_tf.getText(), detail_tf.getText());
        //sending post request
        URL url = new URL("http://localhost:8080/users/" + skill.getId() + "/skills" );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(skill);

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
//            sceneCleaner();
            resultMessage.setText("Skill added successfully");
            title_tf.setText("");
            detail_tf.setText("");

        } else
            resultMessage.setText("Server error");
    }

    public void deleteSkill(String id, String title) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/users/" + id + "/skills/" + title);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("JWT", id);
            con.setRequestMethod("DELETE");

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputline;
            StringBuffer response1 = new StringBuffer();
            while ((inputline = in.readLine()) != null) {
                response1.append(inputline);
            }
            in.close();
            response = response1.toString();

            if (response.equals("userSkill deleted")) {
                resultMessage.setText("UserSkill deleted Successfully");
            } else {
                System.out.println(response);
            }
        } catch (ConnectException e) {
            System.out.println("Connection failed");
        }
    }

    public void editSkill(String id, String previousTitle) throws IOException {
        String response;
        Skill skill = new Skill(id, title_tf.getText(), detail_tf.getText());
        URL url = new URL("http://localhost:8080/users/" + id + "/skills/" + previousTitle);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(skill);
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

        if (response.equals("Skill Updated")) {
            resultMessage.setText("Skill Updated");

        } else {
            resultMessage.setText("Server error during Skill update");
        }
    }

    public ObservableList<String> skillsGetter(String id) throws IOException {

        URL url = new URL("http://localhost:8080/users/" + id + "/skills");
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
        String[] userskills = toStringArray(jsonObject);


        ObservableList<String> skillTitles = FXCollections.observableArrayList();

        for (String jsonSkills : userskills) {
            JSONObject obj = new JSONObject(jsonSkills);
//            Skill skill = new Skill(obj.getString("id"), obj.getString("skillTitle"), obj.getString("SkillDetail"));
            skillTitles.add(obj.getString("skillTitle"));
        }
        Collections.sort(skillTitles);
        return skillTitles;

    }

    public Skill skillGetter(String id, String title) throws IOException {

        URL url = new URL("http://localhost:8080/users/" + id + "/skills/" + title);
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
        JSONObject obj = new JSONObject(response);
        Skill skill = new Skill(obj.getString("id"), obj.getString("skillTitle"), obj.getString("skillDetail"));
        return skill;
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

    public static Education getEducation(String id) throws IOException {
        try {
            URL url = new URL("http://localhost:8080/users/" + id + "/educations");
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
            System.out.println(response);  /////
            if (!response.equals("No Education")) {
                JSONObject obj = new JSONObject(response);
                Education education = new Education(obj.getString("id"), obj.getString("institute"), obj.getString("study"), new java.sql.Date(obj.getLong("startedDate")), new java.sql.Date(obj.getLong("finishedDate")), obj.getString("grade"), obj.getString("activities"), obj.getString("description"));

                return education;
            }
            return null;
        } catch (ConnectException e) {
            return null;
//            return new Education("", "", "", null, null, "", "", "");
        }
    }
    public void setEducationToEditS()throws IOException{
        {
            Education thisEducation = getEducation(MainApplication.loggedInUser.getId());
            if (thisEducation != null) {
                study_tf.setText(thisEducation.getStudy());
                institute_tf.setText(thisEducation.getInstitute());
                grade_tf.setText(thisEducation.getGrade());
                activities_tf.setText(thisEducation.getActivities());
                description_tf.setText(thisEducation.getDescription());
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                finishedDate_tf.setText(sdf.format(thisEducation.getFinishedDate()));
                startedDate_tf.setText(sdf.format(thisEducation.getStartedDate()));
            }
        }
    }
    public void saveEducation(String id) throws IOException {
        String response;

        try {
            Education education = new Education(id, institute_tf.getText(), study_tf.getText(), dateFormatter(startedDate_tf.getText()), dateFormatter(finishedDate_tf.getText()), grade_tf.getText(), activities_tf.getText(), description_tf.getText());

            //sending post request
            URL url = new URL("http://localhost:8080/users/" + id + "/educations");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(education);

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
                resultMessage.setText("Education added successfully");

            } else
                resultMessage.setText("Server error");

        } catch (Exception e) {
            System.out.println("making education to save failed");
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
