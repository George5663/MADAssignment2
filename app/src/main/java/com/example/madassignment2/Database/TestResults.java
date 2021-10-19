package com.example.madassignment2.Database;

public class TestResults {
    private String totalTime;
    private String startTime;
    private int totalScore;

    //Declaring a Student
    public TestResults(int totalScore, String startTime, String totalTime) {

        this.totalScore = totalScore;
        this.startTime =startTime;
        this.totalTime = totalTime;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTotalTime() {
        return totalTime;
    }
}
