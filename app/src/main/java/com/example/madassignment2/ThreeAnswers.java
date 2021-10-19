package com.example.madassignment2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.madassignment2.MathTest;
import java.util.ArrayList;

public class ThreeAnswers extends Fragment {
    private int answer1, answer2, answer3;
    private MathTest test;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_three_answers, container, false);
        answer1 = getArguments().getInt("answer1");
        answer2 = getArguments().getInt("answer2");
        answer3 = getArguments().getInt("answer3");
        Button answer1Button = (Button) view.findViewById(R.id.threeAnswerButton1);
        Button answer2Button = (Button) view.findViewById(R.id.threeAnswerButton2);
        Button answer3Button = (Button) view.findViewById(R.id.threeAnswerButton3);
        answer1Button.setText(String.valueOf(answer1));
        answer2Button.setText(String.valueOf(answer2));
        answer3Button.setText(String.valueOf(answer3));

        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MathTest)getActivity()).answerQuestion(answer1);
            }
        });
        answer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MathTest)getActivity()).answerQuestion(answer2);
            }
        });
        answer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MathTest)getActivity()).answerQuestion(answer3);
            }
        });
        return view;
    }
}