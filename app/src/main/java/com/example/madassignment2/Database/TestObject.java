package com.example.madassignment2.Database;

import java.util.ArrayList;

public class TestObject {
    private String question;
    private ArrayList<Integer> options;
    private int timeToSolve;

    public TestObject(String question, ArrayList<Integer> options, int timeToSolve)
    {
        this.question = question;
        this.options = options;
        this.timeToSolve = timeToSolve;
    }

    public int getTimeToSolve() {
        return timeToSolve;
    }

    public ArrayList<Integer> getOptions() {
        return options;
    }

    public String getQuestion() {
        return question;
    }
}
