package com.example.squadject;

public class JoinItem {
    private String profilePicture;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String college;
    private String branch;
    private String semester;
    private String skills;

    public JoinItem(String profilePicture, String fullName, String email, String phoneNumber, String college, String branch, String semester, String skills) {
        this.profilePicture = profilePicture;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.college = college;
        this.branch = branch;
        this.semester = semester;
        this.skills = skills;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCollege() {
        return college;
    }

    public String getBranch() {
        return branch;
    }

    public String getSemester() {
        return semester;
    }

    public String getSkills() {
        return skills;
    }
}