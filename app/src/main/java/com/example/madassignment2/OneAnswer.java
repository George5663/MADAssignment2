package com.example.madassignment2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.madassignment2.MathTest;
import java.util.ArrayList;

public class OneAnswer extends Fragment {
    private int answer1;
    private MathTest test;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_answer, container, false);
        answer1 = getArguments().getInt("answer1");
        Button answer1Button = (Button) view.findViewById(R.id.oneAnswerButton);
        answer1Button.setText(String.valueOf(answer1));

        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MathTest)getActivity()).answerQuestion(answer1);
            }
        });
        return view;
    }
}