package com.example.cosc2657_assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosc2657_assignment1.Answer.Answer;
import com.example.cosc2657_assignment1.Question.Question;
import com.example.cosc2657_assignment1.Quiz.Quiz;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class takeTestActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    FloatingActionButton nextButton;

    TextView questionName;

    RecyclerView questionItems;
    QuestionTestContainerAdapter adapter;

    TextView counterText;

    Quiz quiz;
    Quiz quizCopy;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cRef = db.collection(Quiz.CollectionName);
    DocumentReference dRef;

    int questionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);


        //////////GET VIEW
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        counterText = findViewById(R.id.questioncounter);

        questionName = findViewById(R.id.questionDescription);
        questionItems = findViewById(R.id.questionItems);
        questionItems.setLayoutManager(new LinearLayoutManager(this));

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        //GET INTENT QUIZ
        Intent intent = getIntent();

        //HAS TO BE THE FIRST INIT OF DATA
        quiz = (Quiz) intent.getExtras().get("quiz");
        //make new copy to check answers
        quizCopy = new Quiz(quiz);
        for (Question question: quiz.getQuestions()){
            for (Answer answer: question.getCorrectAnswers()){
                answer.setCorrect(false);
            }
        }
        dRef = cRef.document(quiz.getQid());

        if (quiz == null){
            Toast.makeText(this, "Error, could not find this quiz", Toast.LENGTH_SHORT).show();
            finish();
        }

        //init question to make sure it is not null
        if (quiz.getQuestions().size() == 0){
            quiz.addQuestion(0);
        }

        //set adapter for recycler view
        adapter = new QuestionTestContainerAdapter(this, quiz.getQuestions().get(questionIndex).getAnswers());
        questionItems.setAdapter(adapter);

        //ON CLICK LISTENER
        backButton.setOnClickListener(v -> {
            if (questionIndex == -1){
                Toast.makeText(this, "You do not have any questions for this quiz yet!", Toast.LENGTH_SHORT).show();
            }
            else if (questionIndex == 0){
                Toast.makeText(this, "This is the first quiz already!", Toast.LENGTH_SHORT).show();
            }
            //handle back button
            else {
                questionIndex -= 1;
                refreshFragment();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (questionIndex == -1){
                Toast.makeText(this, "You do not have any questions for this quiz yet!", Toast.LENGTH_SHORT).show();
            }
            else if (questionIndex >= quiz.getQuestions().size() - 1){
                Toast.makeText(this, "This is the last quiz already!", Toast.LENGTH_SHORT).show();            }
            else {
                questionIndex += 1;
                refreshFragment();
            }
        });

        refreshFragment();
    }

    void refreshFragment(){
        if (questionIndex < 0 || questionIndex >= quiz.getQuestions().size()) return;

        if (quiz.getQuestions().get(questionIndex).getAnswers().size() == 0){
            quiz.getQuestions().get(questionIndex).getAnswers().add(new Answer());
        }

        questionName.setText(quiz.getQuestions().get(questionIndex).getQuestionName());

        adapter.setData(quiz.getQuestions().get(questionIndex).getAnswers());

        setCounterText();
        System.out.println("refreshed");
    }

    void setCounterText(){
        String res = "Question " + (questionIndex + 1) + " of " + quiz.getQuestions().size();
        counterText.setText(res);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_quiz, menu);
        return true;
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exiting");
            builder.setMessage("You will lose your quiz progress.");
            builder.setPositiveButton("confirm", null);
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                dialog.dismiss();
                finish();
            });
            return true;
        }
        else if (item.getItemId() == R.id.action_submit){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submit");
            builder.setMessage("Are you sure you want to submit?");
            builder.setPositiveButton("confirm", null);
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                dialog.dismiss();
                //submitted, now we compare it with quizCopy
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Results");
                builder1.setMessage("You got " + quizCopy.countCorrectAnswers(quiz) + " out of " + quiz.getQuestions().size() + " questions correct!");
                builder1.setPositiveButton("confirm", null);
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v2 -> {
                    dialog1.dismiss();
                    finish();
                });
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}