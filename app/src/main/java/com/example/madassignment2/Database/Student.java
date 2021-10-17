package com.example.madassignment2.Database;

public class Student {
    private String firstName;
    private String email;
    private String lastName;
    private int phoneNumber;
    private String studentPicture;

    //Declaring a Student
    public Student(String firstName, String lastName, String email, int phoneNumber, String studentPicture) {
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.studentPicture = studentPicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getStudentPicture() {
        return studentPicture;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStudentPicture(String studentPicture) {
        this.studentPicture = studentPicture;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
