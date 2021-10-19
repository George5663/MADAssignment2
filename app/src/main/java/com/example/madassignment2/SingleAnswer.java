package com.example.madassignment2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madassignment2.MathTest;

public class SingleAnswer extends Fragment {
    private MathTest test;
    public SingleAnswer() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_answer, container, false);
        Button submitAnswerButton = (Button) view.findViewById(R.id.submitAnswer);
        EditText answer = (EditText) view.findViewById(R.id.answerEditText);

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer.getText().toString().matches(""))
                {
                    Toast.makeText(getActivity(), "You cannot submit an empty answer", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((MathTest) getActivity()).answerQuestion(Integer.parseInt(answer.getText().toString()));
                }
            }
        });
        return view;
    }

}