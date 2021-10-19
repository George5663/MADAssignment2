package com.example.madassignment2;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.madassignment2.Database.TestList;
import com.example.madassignment2.Database.TestObject;
import com.example.madassignment2.Database.TestResults;

import javax.net.ssl.HttpsURLConnection;

public class MathTest extends AppCompatActivity {
    private String result;
    private TextView questionArea, totalScore, timePassed, timerQuestion;
    private int testResult, answersLeft = 0, placeInArray = 0, currScore = 0, answer1, answer2, answer3, answer4;
    private LocalTime startTime;
    private ArrayList<Integer> options;
    private TestObject test;
    private Timer timer, currTimer;
    private Button nextButton, previousButton;
    private TestList testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_question);
        testList = new TestList();
        testList.load(getApplicationContext());
        questionArea = findViewById(R.id.questionArea);
        totalScore = findViewById(R.id.totalScore);
        timePassed = findViewById(R.id.totalTimePassed);
        timerQuestion = findViewById(R.id.timer);
        Button passButton = findViewById(R.id.passButton);
        nextButton = findViewById(R.id.nextBtn);
        Button endButton = findViewById(R.id.endButton);
        previousButton = findViewById(R.id.previousBtn);


        new Task().execute();

        startTime = LocalTime.now();

        currTimer = new Timer();
        currTimer.scheduleAtFixedRate(new TimerTask() {
            int currTime = 0;

            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timePassed.setText("Duration: " + currTime);
                    }
                });
                currTime++;
            }
        }, 0, 1000);
        previousButton.setEnabled(false);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeInArray > 3) {
                    answersLeft = Math.min(test.getOptions().size(), answersLeft + 8);
                    placeInArray = Math.max(0, placeInArray - 8);
                    startAnswersFragment();
                } else {
                    Toast.makeText(getApplicationContext(), "You're at the start", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answersLeft > 0) {
                    previousButton.setEnabled(true);
                    startAnswersFragment();
                } else {
                    Toast.makeText(getApplicationContext(), "No more questions", Toast.LENGTH_SHORT).show();
                }
            }
        });
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButton.setEnabled(false);
                placeInArray = 0;
                new Task().execute();
            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), testList.add(new TestResults(currScore, startTime.toString(), timePassed.getText().toString())), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MathTest.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    public void answerQuestion(int answer) {
        if (testResult == answer) {
            currScore = currScore + 10;
        } else {
            currScore = currScore - 5;
        }
        totalScore.setText("Total Score: " + currScore);
        previousButton.setEnabled(false);
        placeInArray = 0;
        new Task().execute();
    }

    private class Task extends AsyncTask<Void, Integer, TestObject> {
        private int totalBytes;

        @Override
        protected TestObject doInBackground(Void... voids) {
            String urlString = Uri.parse("https://10.0.2.2:8000/random/question/")
                    .buildUpon()
                    .appendQueryParameter("method", "thedata.getit")
                    .appendQueryParameter("api_key", "01189998819991197253")
                    .appendQueryParameter("format", "json")
                    .build().toString();
            try {
                URL url = new URL(urlString);

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                DownloadUtils.addCertificate(MathTest.this, conn);

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException();
                } else {
                    InputStream input = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    totalBytes = conn.getContentLength();
                    int currBytes = 0;
                    byte[] buffer = new byte[1024];
                    int bytesRead = input.read(buffer);
                    while (bytesRead > 0) {
                        baos.write(buffer, 0, bytesRead);
                        bytesRead = input.read(buffer);
                        currBytes += bytesRead;
                        publishProgress(currBytes);
                    }
                    baos.close();
                    result = baos.toString();

                    JSONObject jBase = new JSONObject(result);
                    JSONArray jOptions = jBase.getJSONArray("options");
                    if (jOptions.length() == 1) {

                    }
                    String question = (String) jBase.getString("question");
                    testResult = (int) jBase.getInt("result");

                    options = new ArrayList<>(jOptions.length());
                    for (int i = 0; i < jOptions.length(); i++) {
                        options.add(jOptions.getInt(i));
                    }

                    int timeToSolve = (int) jBase.getInt("timetosolve");
                    test = new TestObject(question, options, timeToSolve);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (RuntimeException e) {
                System.out.println("Response code isn't valid");
                System.exit(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return test;
        }

        @Override
        protected void onPostExecute(TestObject resultIn) {
            questionArea.setText(resultIn.getQuestion());
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                int time = resultIn.getTimeToSolve();

                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerQuestion.setText("Time Left: " + time);
                        }
                    });
                    time--;

                    if (time < 0) {
                        timer.cancel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Task().execute();
                            }
                        });
                    }
                }
            }, 0, 1000);
            answersLeft = resultIn.getOptions().size();


            startAnswersFragment();
        }
    }

    public void startAnswersFragment() {
        if (answersLeft <= 4) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setReorderingAllowed(true);
        if (answersLeft == 1) {
            OneAnswer oneAnswerFrag = new OneAnswer();
            Bundle bundle = new Bundle();
            answer1 = test.getOptions().get(placeInArray);
            placeInArray++;
            bundle.putInt("answer1", answer1);
            oneAnswerFrag.setArguments(bundle);
            answersLeft = answersLeft - 1;
            ft.replace(R.id.fragmentContainer, oneAnswerFrag).commit();
        }
        if (answersLeft == 0) {
            SingleAnswer singleAnswerFrag = new SingleAnswer();
            ft.replace(R.id.fragmentContainer, singleAnswerFrag, null).commit();
        }
        if (answersLeft == 2) {
            TwoAnswers twoAnswerFrag = new TwoAnswers();
            Bundle bundle = new Bundle();
            answer1 = test.getOptions().get(placeInArray);
            placeInArray++;
            answer2 = test.getOptions().get(placeInArray);
            placeInArray++;
            bundle.putInt("answer1", answer1);
            bundle.putInt("answer2", answer2);
            twoAnswerFrag.setArguments(bundle);
            answersLeft = answersLeft - 2;
            ft.replace(R.id.fragmentContainer, twoAnswerFrag).commit();

        } else if (answersLeft == 3) {
            ThreeAnswers threeAnswerFrag = new ThreeAnswers();
            Bundle bundle = new Bundle();
            answer1 = test.getOptions().get(placeInArray);
            placeInArray++;
            answer2 = test.getOptions().get(placeInArray);
            placeInArray++;
            answer3 = test.getOptions().get(placeInArray);
            placeInArray++;
            bundle.putInt("answer1", answer1);
            bundle.putInt("answer2", answer2);
            bundle.putInt("answer3", answer3);
            threeAnswerFrag.setArguments(bundle);
            answersLeft = answersLeft - 3;
            ft.replace(R.id.fragmentContainer, threeAnswerFrag).commit();

        } else if (answersLeft >= 4) {
            FourOrMoreAnswers fourAnswerFrag = new FourOrMoreAnswers();
            //fourAnswerFrag = new FourOrMoreAnswers();
            Bundle bundle = new Bundle();
            answer1 = test.getOptions().get(placeInArray);
            placeInArray++;
            answer2 = test.getOptions().get(placeInArray);
            placeInArray++;
            answer3 = test.getOptions().get(placeInArray);
            placeInArray++;
            answer4 = test.getOptions().get(placeInArray);
            placeInArray++;
            bundle.putInt("answer1", answer1);
            bundle.putInt("answer2", answer2);
            bundle.putInt("answer3", answer3);
            bundle.putInt("answer4", answer4);
            fourAnswerFrag.setArguments(bundle);
            answersLeft = answersLeft - 4;
            ft.replace(R.id.fragmentContainer, fourAnswerFrag).commit();

        }
    }
}


