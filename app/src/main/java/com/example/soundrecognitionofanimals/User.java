package com.example.soundrecognitionofanimals;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String utaId;
    private String profession;
    private String securityQuestion;
    private String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String firstName, String lastName, String utaId, String profession, String password, String securityQuestion) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.utaId = utaId;
        this.profession = profession;
        this.password = password;
        this.securityQuestion = securityQuestion;
    }

    // Getter and Setter methods for the 'email' field
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter methods for the 'firstName' field
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter and Setter methods for the 'lastName' field
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter and Setter methods for the 'utaId' field
    public String getUtaId() {
        return utaId;
    }

    public void setUtaId(String utaId) {
        this.utaId = utaId;
    }

    // Getter and Setter methods for the 'profession' field
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String email) {
        this.password = password;
    }

    // Getter and Setter methods for the 'securityQuestion' field
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
}

