package com.linkedin.clientapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

import static java.lang.System.in;

public class ProfileController implements Initializable {

    @FXML
    private ToggleButton Follow_bt;
    @FXML
    private Label dialougeTitle;
    @FXML
    private Label activities_tf;

    @FXML
    private Label description_tf;
    @FXML
    private TextArea dialouge_tf;

    @FXML
    private Label FullName_tf;

    @FXML
    private Button Profile_bt;

    @FXML
    private TextArea detail_tf;

    @FXML
    private Label finishedDate_tf;

    @FXML
    private Label followers_tf;

    @FXML
    private Label followings_tf;

    @FXML
    private Label grade_tf;

    @FXML
    private AnchorPane education_anchor;

    @FXML
    private Button home_bt;

    @FXML
    private ImageView image_view;

    @FXML
    private Label institute_tf;

    @FXML
    private Button logout_bt;

    @FXML
    private Button message_bt;

    @FXML
    private Circle profile_img;

    @FXML
    private Button search_bt;

    @FXML
    private ListView skillListView;

    @FXML
    private Label startedDate_tf;

    @FXML
    private Label study_tf;

    @FXML
    private TextField title_tf;

    @FXML
    private Label username_tf;

    String selectedTitle;

    User searchedUser = MainApplication.searchedUser;

    Image AvatarImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            if (MainApplication.searchedUser == null || searchedUser.getId().equals(MainApplication.loggedInUser.getId())) {
                Follow_bt.setVisible(false);
                message_bt.setVisible(false);
                User thisuser = getUser(MainApplication.loggedInUser.getId());
                username_tf.setText(thisuser.getId());
                FullName_tf.setText(thisuser.getFirstName() + " " + thisuser.getLastName());
                followers_tf.setText(String.valueOf(numFollowers(thisuser)));
                followings_tf.setText(String.valueOf(numFollowing(thisuser)));

                AvatarImage = getAvatar(thisuser);
                image_view = new ImageView(AvatarImage);
                image_view.setFitHeight(75);
                image_view.setFitHeight(75);
                profile_img.setFill(new ImagePattern(AvatarImage));

                skillListView.setItems(skillsGetter(thisuser.getId()));
                skillListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        selectedTitle = skillListView.getSelectionModel().getSelectedItem().toString();
                        try {
                            Skill skillSelected = skillGetter(thisuser.getId(), selectedTitle);
                            detail_tf.setText(skillSelected.getSkillDetail());
                            title_tf.setText(selectedTitle);
                            title_tf.setVisible(true);
                            detail_tf.setVisible(true);

                        } catch (IOException e) {
                            System.out.println("finding user Skill failed");
                        }
                    }
                });
                dialouge_tf.setVisible(false);
                dialougeTitle.setVisible(false);

                setEducationToProf(thisuser.getId());

//            profile_img.path
            } else {
                searchedUser = getUser(MainApplication.searchedUser.getId());
                username_tf.setText(searchedUser.getId());
                FullName_tf.setText(searchedUser.getFirstName() + " " + searchedUser.getLastName());
                followers_tf.setText(String.valueOf(numFollowers(searchedUser)));
                followings_tf.setText(String.valueOf(numFollowing(searchedUser)));
                setAction();
                if (IsFollowing(MainApplication.loggedInUser, searchedUser)) {
                    Follow_bt.setText("UnFollow");
                    Follow_bt.setSelected(true);
                }
                AvatarImage = getAvatar(searchedUser);
                image_view = new ImageView(AvatarImage);
                image_view.setFitHeight(75);
                image_view.setFitHeight(75);
                profile_img.setFill(new ImagePattern(AvatarImage));

                skillListView.setItems(skillsGetter(searchedUser.getId()));
                skillListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        selectedTitle = skillListView.getSelectionModel().getSelectedItem().toString();
                        try {
                            Skill skillSelected = skillGetter(searchedUser.getId(), selectedTitle);
                            detail_tf.setText(skillSelected.getSkillDetail());
                            title_tf.setText(selectedTitle);
                            title_tf.setVisible(true);
                            detail_tf.setVisible(true);

                        } catch (IOException e) {
                            System.out.println("finding user Skill failed");
                        }
                    }
                });

                setEducationToProf(searchedUser.getId());

//            profile_img.path

            }
        } catch (IOException exception) {
            System.out.println("Unable to get user Data");
        }
    }

    private void setAction() {
        message_bt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                MainApplication m = new MainApplication();
//                DirectView_controller.user = user;
//                try {
//                    m.changeScene(11);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });
        image_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            }
        });
        Follow_bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (!IsFollowing(MainApplication.loggedInUser, searchedUser)) {
                        try {
                            String response;
                            URL url = new URL("http://localhost:8080/follows/" + MainApplication.loggedInUser.getId() + "/" + searchedUser.getId());
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");
                            con.setRequestProperty("JWT", MainApplication.userToken);

                            int responseCode = con.getResponseCode();
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputline;
                            StringBuffer response1 = new StringBuffer();
                            while ((inputline = in.readLine()) != null) {
                                response1.append(inputline);
                            }
                            in.close();
                            response = response1.toString();

                            if (response.equals("Done!")) {
                                System.out.println("Followed");
                                Follow_bt.setText("UnFollow");
                            } else {
                                System.out.println(response);
                            }

                        } catch (ConnectException e) {
                            System.out.println("Connection failed");
                        }
                    } else {

                        try {
                            System.out.println("checking2");
                            String response;
                            URL url = new URL("http://localhost:8080/follows/" + MainApplication.loggedInUser.getId() + "/" + searchedUser.getId());
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestProperty("JWT", MainApplication.userToken);
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

                            if (response.equals("Done!")) {
                                System.out.println("UnFollowed");
                                Follow_bt.setText("Follow");
                                Follow_bt.setSelected(false);
                            } else {
                                System.out.println(response);
                            }
                        } catch (ConnectException e) {
                            System.out.println("Connection failed");
                        }
                    }

                    followers_tf.setText(Integer.toString(numFollowers(searchedUser)));
                    followings_tf.setText(Integer.toString(numFollowing(searchedUser)));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
//        followers_tf.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//             public void handle(MouseEvent mouseEvent) {
//                UserView_controller.users = new ArrayList<>();
//                try {
//                    String response;
//                    URL url = new URL("http://localhost:8080/follows");
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("GET");
//                    int responseCode = con.getResponseCode();
//                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String inputline;
//                    StringBuffer response1 = new StringBuffer();
//                    while ((inputline = in.readLine()) != null) {
//                        response1.append(inputline);
//                    }
//                    in.close();
//                    response = response1.toString();
//
//                    JSONArray jsonObject = new JSONArray(response);
//                    String[] follows = toStringArray(jsonObject);
//                    for (String t : follows) {
//                        JSONObject obj = new JSONObject(t);
//                        Follow f = new Follow(obj.getString("follower"), obj.getString("followed"));
//                        if (f.getFollowed().equals(user.getId())) {
//                            UserView_controller.users.add(getUser(f.getFollower()));
//                        }
//                    }
//                    HelloApplication m = new HelloApplication();
//                    m.changeScene(9);
//                } catch (IOException e) {
//                    System.out.println("Connection failed");
//                }
//            }
//        });
//        followings_tf.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                UserView_controller.users = new ArrayList<>();
//                try {
//                    String response;
//                    URL url = new URL("http://localhost:8080/follows");
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("GET");
//                    int responseCode = con.getResponseCode();
//                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String inputline;
//                    StringBuffer response1 = new StringBuffer();
//                    while ((inputline = in.readLine()) != null) {
//                        response1.append(inputline);
//                    }
//                    in.close();
//                    response = response1.toString();
//
//                    JSONArray jsonObject = new JSONArray(response);
//                    String[] follows = toStringArray(jsonObject);
//                    for (String t : follows) {
//                        JSONObject obj = new JSONObject(t);
//                        Follow f = new Follow(obj.getString("follower"), obj.getString("followed"));
//                        if (f.getFollower().equals(user.getId())) {
//                            UserView_controller.users.add(getUser(f.getFollowed()));
//                        }
//                    }
//                    HelloApplication m = new HelloApplication();
//                    m.changeScene(9);
//                } catch (IOException e) {
//                    System.out.println("Connection failed");
//                }
//            }
//        });
    }

    public void logoutClicked(ActionEvent event) throws Exception {

        MainApplication app = new MainApplication();
        app.logedOut();
        app.changeCurrentScene("SignIn");

    }

    public void setSearch_bt(MouseEvent event) throws Exception {
        MainApplication app = new MainApplication();
        app.changeCurrentScene("Search");

    }

    public void setProfile_img(MouseEvent event) throws Exception {

        MainApplication.searchedUser = null;

        MainApplication app = new MainApplication();
        app.changeCurrentScene("EditProfile");

    }

    public void setProfile_bt(ActionEvent event) throws Exception {

        MainApplication.searchedUser = null;

        MainApplication app = new MainApplication();
        app.changeCurrentScene("Profile");

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

            User user = new User(obj.getString("id"), obj.getString("firstName"), obj.getString("lastName"), obj.getString("email"), obj.getString("phoneNumberType"), obj.getString("phoneNumber"), obj.getString("password"), obj.getString("country"), obj.getString("city"), new Date(obj.getLong("birthday")), obj.getString("socialLink"), new Date(obj.getLong("userCreatedAt")));
            return user;
        } catch (ConnectException e) {
            return new User("", "", "", "", "", "", "", "", "", new Date(), "", new Date());
        }
    }

    public static Image getAvatar(User user) throws IOException {
        try {


            String response;
            URL url = new URL("http://localhost:8080/media/" + user.getId() + "/Avatar/png");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            String t = con.getHeaderField("content-length");

            if (t == null || Integer.parseInt(t) == 7) {
                Image image = new Image(Path.of("src/main/resources/com/linkedin/clientapp/basePictures/user.png").toUri().toString());
                return image;
            }

            Files.copy(con.getInputStream(), Path.of("src/main/resources/com/linkedin/clientapp/basePictures/user.png"), StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image(Path.of("src/main/resources/com/linkedin/clientapp/basePictures/user.png").toUri().toString());
            return image;

        } catch (ConnectException e) {
            Image image = new Image(Path.of(".../resources/com/linkedin/clientapp/basePictures/default.png").toUri().toString());
            return image;
        }
    }

    public int numFollowers(User user) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/follows");
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

            JSONArray jsonObject = new JSONArray(response);
            String[] follows = toStringArray(jsonObject);
            int res = 0;
            for (String t : follows) {
                JSONObject obj = new JSONObject(t);
                Follow f = new Follow(obj.getString("follower"), obj.getString("followed"));
                if (f.getFollowed().equals(user.getId()))
                    res++;
            }
            return res;
        } catch (ConnectException e) {
            System.out.println("Connection failed");
            return 0;
        }
    }

    public int numFollowing(User user) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/follows");
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

            JSONArray jsonObject = new JSONArray(response);
            String[] follows = toStringArray(jsonObject);
            int res = 0;
            for (String t : follows) {
                JSONObject obj = new JSONObject(t);
                Follow f = new Follow(obj.getString("follower"), obj.getString("followed"));
                if (f.getFollower().equals(user.getId()))
                    res++;
            }
            return res;
        } catch (ConnectException e) {
            System.out.println("Connection failed");
            return 0;
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

    public void setEducationToProf(String id) throws IOException {
        Education thisEducation = getEducation(id);
        if (thisEducation != null) {
            study_tf.setText(thisEducation.getStudy());
            institute_tf.setText(thisEducation.getInstitute());
            grade_tf.setText(thisEducation.getGrade());
//                activities_tf.setText(thisEducation.getActivities());
//                description_tf.setText(thisEducation.getDescription());
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

            finishedDate_tf.setText(sdf.format(thisEducation.getFinishedDate()));
            startedDate_tf.setText(sdf.format(thisEducation.getStartedDate()));
            education_anchor.setVisible(true);

            activities_tf.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    dialouge_tf.setVisible(true);
                    dialougeTitle.setVisible(true);
                    dialougeTitle.setText("Education Activities");
                    dialouge_tf.setText(thisEducation.getActivities());
                }
            });
            description_tf.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    dialouge_tf.setVisible(true);
                    dialougeTitle.setVisible(true);
                    dialougeTitle.setText("Education Description");
                    dialouge_tf.setText(thisEducation.getDescription());

                }
            });

        } else {
            education_anchor.setVisible(false);
        }
    }

    public Education getEducation(String id) throws IOException {
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


    public static String[] toStringArray(JSONArray array) {
        if (array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = array.optString(i);
        return arr;
    }

    public boolean IsFollowing(User follower, User following) throws IOException {
        try {
            String response;
            URL url = new URL("http://localhost:8080/follows");
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

            JSONArray jsonObject = new JSONArray(response);
            String[] follows = toStringArray(jsonObject);
            for (String t : follows) {
                JSONObject obj = new JSONObject(t);
                Follow f = new Follow(obj.getString("follower"), obj.getString("followed"));
                if (f.getFollower().equals(follower.getId()) && f.getFollowed().equals(following.getId()))
                    return true;
            }
            return false;
        } catch (ConnectException e) {
            System.out.println("Connection failed");
            return false;
        }
    }

}
