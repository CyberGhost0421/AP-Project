package Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class User {
    @JsonProperty("id")
    private String id;
    @JsonProperty("token")
    private String token;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("phoneNumberType")
    private String phoneNumberType;
    @JsonProperty("password")
    private String password;

    @JsonProperty("country")
    private String country;
    @JsonProperty("city")
    private String city;

    @JsonProperty("birthday")
    private Date birthday;

    @JsonProperty("userCreatedAt")
    private Date userCreatedAt;

    @JsonProperty("socialLink")
    private String socialLink;

    public User(String id, String firstName, String lastName, String email, String phoneNumberType, String phoneNumber, String password, String country, String city, Date birthday,  String socialLink , Date userCreatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumberType = phoneNumberType;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.country = country;
        this.city = city;
        this.birthday = birthday;
        this.userCreatedAt = userCreatedAt;
        this.socialLink = socialLink;
    }

//    public User(String id, String firstName, String lastName, String email, String phoneNumber, String password, String country, Date birthday) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.password = password;
//        this.country = country;
//        this.birthday = birthday;
//        this.userCreatedAt = new Date(System.currentTimeMillis());
//    }

    public User() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(String phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(Date userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumberType=" + phoneNumberType + ':' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", country='" + country + '\'' +
                ", city=" + city + '\'' +
                ", birthday=" + birthday + '\'' +
                ", createdAt=" + userCreatedAt + '\'' +
                ", socialLink=" + socialLink +
                '}';
    }
}
