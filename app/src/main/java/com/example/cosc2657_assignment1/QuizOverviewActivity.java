package com.example.cosc2657_assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosc2657_assignment1.Quiz.Quiz;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizOverviewActivity extends AppCompatActivity {

    TextView description;
    TextView questionCount;
    Quiz quiz;
    FloatingActionButton editQuiz;
    FloatingActionButton editQuizName;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cRef = db.collection(Quiz.CollectionName);
    DocumentReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_overview);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        quiz = (Quiz) intent.getExtras().get("quiz");

        description = findViewById(R.id.Description);
        questionCount = findViewById(R.id.QuestionCount);
        editQuiz = findViewById(R.id.editButton);
        editQuizName = findViewById(R.id.editQuizNameButton);

        dRef = cRef.document(quiz.getQid());

        description.setText(quiz.getQuizName());
        questionCount.setText(String.valueOf(quiz.getQuestions().size()));

        editQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(QuizOverviewActivity.this, QuizEditActivity.class);
                editIntent.putExtra("quiz", quiz);
                startActivity(editIntent);
            }
        });

        editQuizName.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizOverviewActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Quiz Name");
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(quiz.getQuizName());
            builder.setView(input);
            builder.setPositiveButton("confirm", null);
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {});

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                String res = input.getText().toString();
                if (res.length() < 4 || res.length() > 35) {
                    Toast.makeText(v1.getContext(), "Quiz name has to be at least 5 and at most 35 characters!", Toast.LENGTH_LONG).show();
                    return;
                }
                quiz.setQuizName(input.getText().toString());
                description.setText(quiz.getQuizName());
                dRef.update(Quiz.QUIZNAME_, quiz.getQuizName());
                dialog.dismiss();
            });
        });


    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}