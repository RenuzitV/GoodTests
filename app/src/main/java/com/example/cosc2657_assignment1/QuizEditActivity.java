package com.example.cosc2657_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc2657_assignment1.Answer.Answer;
import com.example.cosc2657_assignment1.Quiz.Quiz;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizEditActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    FloatingActionButton removeButton;
    FloatingActionButton addButton;
    FloatingActionButton nextButton;

    Button addAnswerButton;
    Button removeAnswerButton;

    EditText questionName;

    RecyclerView questionItems;
    QuestionContainerAdapter adapter;

    TextView counterText;

    Quiz quiz;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cRef = db.collection(Quiz.CollectionName);
    DocumentReference dRef;

    int questionIndex = 0;

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

        counterText = findViewById(R.id.questioncounter);

        questionName = findViewById(R.id.questionDescription);
        questionItems = findViewById(R.id.questionItems);
        questionItems.setLayoutManager(new LinearLayoutManager(this));

        addAnswerButton = findViewById(R.id.buttonAddAnswer);
        removeAnswerButton = findViewById(R.id.buttonRemoveAnswer);
        //////////GET VIEW

        //GET INTENT QUIZ
        Intent intent = getIntent();

        //HAS TO BE THE FIRST INIT OF DATA
        quiz = (Quiz) intent.getExtras().get("quiz");
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
        adapter = new QuestionContainerAdapter(this, quiz.getQuestions().get(questionIndex).getAnswers());
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
                questionIndex = quiz.getQuestions().size() - 1;
            }
            else {
                questionIndex += 1;
                refreshFragment();
            }
        });

        removeButton.setOnClickListener(v -> {
            if (questionIndex < 0 || questionIndex >= quiz.getQuestions().size() || quiz.getQuestions().size() == 1) return;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete");
            builder.setMessage("Do you want to delete this question?");
            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                quiz.getQuestions().remove(questionIndex);
                if (questionIndex >= quiz.getQuestions().size()) questionIndex--;
                refreshFragment();
            });
            builder.setNegativeButton(android.R.string.no, (dialog, which) -> {});

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        addButton.setOnClickListener(v -> {
            if (quiz.getQuestions().size() > 20) {
                Toast.makeText(this, "You have too many questions already!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (questionIndex == -1) questionIndex = 0;
            quiz.addQuestion(questionIndex + 1);
            ++questionIndex;
            refreshFragment();
        });

        //ON CLICK LISTENER

        //listener for questionName
        questionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                quiz.getQuestions().get(questionIndex).setQuestionName(s.toString());
            }
        });

        addAnswerButton.setOnClickListener(v -> adapter.addItem(quiz.getQuestions().get(questionIndex).getAnswers().size()));

        removeAnswerButton.setOnClickListener(v -> {
            if (quiz.getQuestions().get(questionIndex).getAnswers().size() > 1) adapter.removeItem(quiz.getQuestions().get(questionIndex).getAnswers().size()-1);
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exiting");
            builder.setMessage("You will lose all unsaved changes.");
            builder.setPositiveButton("confirm", null);
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {});

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                dialog.dismiss();
                finish();
            });
            return true;
        }
        else if (item.getItemId() == R.id.action_save){
            dRef.set(quiz).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent();
                    intent.putExtra("quiz", quiz);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(this, "Quiz saved successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Quiz save failed!", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}