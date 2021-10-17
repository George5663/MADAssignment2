package com.example.madassignment2.Database;

public class StudentSchema {
    public static class StudentTable {
        //Declaring the columns in the student database
        public static final String name = "student";

        public static class columns {
            public static final String firstName = "studentFirstName";
            public static final String lastName = "studentLastName";
            public static final String phoneNumber = "studentPhoneNumber";
            public static final String email = "studentEmail";
            public static final String studentPhoto = "studentPhoto";
        }
    }
}
