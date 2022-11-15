package com.example.cosc2657_assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cosc2657_assignment1.Question.QuestionViewModel;
import com.example.cosc2657_assignment1.Quiz.Quiz;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QuizEditActivity extends AppCompatActivity {


    FloatingActionButton backButton;
    FloatingActionButton removeButton;
    FloatingActionButton addButton;
    FloatingActionButton nextButton;

    QuestionViewModel viewModel;

    Quiz quiz;

    int questionIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_edit);

        //////////GET VIEW
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        backButton = findViewById(R.id.backButton);
        removeButton = findViewById(R.id.removeButton);
        addButton = findViewById(R.id.addButton);
        nextButton = findViewById(R.id.nextButton);
        //////////GET VIEW

        ///VIEW MODEL FOR QUESTION
        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        //GET INTENT QUIZ
        Intent intent = getIntent();

        //HAS TO BE THE FIRST INIT OF DATA
        quiz = (Quiz) intent.getExtras().get("quiz");

        //save question back to list of questions
        viewModel.getSelectedItem().observe(this, item -> {
            if (questionIndex >= 0 && questionIndex < quiz.getQuestions().size()){
                quiz.getQuestions().set(questionIndex, item);
            }
        });

        if (questionIndex >= 0 && questionIndex < quiz.getQuestions().size()) {
            viewModel.selectItem(quiz.getQuestions().get(questionIndex));
        }

        if (quiz == null){
            Toast.makeText(this, "Error, could not find this quiz", Toast.LENGTH_LONG).show();
            finish();
        }

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
                viewModel.selectItem(quiz.getQuestions().get(questionIndex));
            }
        });

        nextButton.setOnClickListener(v -> {
            if (questionIndex == -1){
                Toast.makeText(this, "You do not have any questions for this quiz yet!", Toast.LENGTH_SHORT).show();
            }
            else if (questionIndex >= quiz.getQuestions().size() - 1){
                Toast.makeText(this, "This is the last quiz already!", Toast.LENGTH_SHORT).show();
                questionIndex = quiz.getQuestions().size() - 1;
            }
            else {
                questionIndex += 1;
                viewModel.selectItem(quiz.getQuestions().get(questionIndex));
            }
        });

        removeButton.setOnClickListener(v -> {
            if (questionIndex < 0 || questionIndex >= quiz.getQuestions().size()) return;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete");
            builder.setMessage("Do you want to delete this question?");
            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                quiz.getQuestions().remove(questionIndex);
                questionIndex -= 1;
                viewModel.selectItem(quiz.getQuestions().get(questionIndex));
            });
            builder.setNegativeButton(android.R.string.no, (dialog, which) -> {});

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        addButton.setOnClickListener(v -> {
            if (quiz.getQuestions().size() > 20) {
                Toast.makeText(this, "You have too many questions already!\nConsider making a new quiz!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (questionIndex == -1) questionIndex = 0;
            quiz.addQuestion(questionIndex);
            viewModel.selectItem(quiz.getQuestions().get(questionIndex));
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