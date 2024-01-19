/*
 * Users.java
 * Model class representing user information.
 */

package com.example.study;

public class Users {

    // Default constructor required for Firebase
    public Users() {
    }

    // Parameterized constructor for creating user instances
    public Users(String userId, String userName, String mail, String password, String profilepic, String status) {
        this.userId = userId;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.profilepic = profilepic;
        this.status = status;
    }

    // Private fields representing user attributes
    private String profilepic, mail, userName, password, userId, status;

    // Getter and setter methods for accessing and updating user attributes

    // Get the profile picture URL
    public String getProfilepic() {
        return profilepic;
    }

    // Set the profile picture URL
    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    // Get the user's email
    public String getMail() {
        return mail;
    }

    // Set the user's email
    public void setMail(String mail) {
        this.mail = mail;
    }

    // Get the user's username
    public String getUserName() {
        return userName;
    }

    // Set the user's username
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Get the user's password
    public String getPassword() {
        return password;
    }

    // Set the user's password
    public void setPassword(String password) {
        this.password = password;
    }

    // Get the user's unique ID
    public String getUserId() {
        return userId;
    }

    // Set the user's unique ID
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Get the user's status
    public String getStatus() {
        return status;
    }

    // Set the user's status
    public void setStatus(String status) {
        this.status = status;
    }
}
