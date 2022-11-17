package com.example.cosc2657_assignment1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cosc2657_assignment1.Question.Question;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionEditFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    EditText questionDescription;

    QuestionContainerAdapter adapter;

    Question question;

    public QuestionEditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuestionEditFragment newInstance() {
        QuestionEditFragment fragment = new QuestionEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable("question");
        }
        else {
            question = new Question();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_quiz_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionDescription = view.findViewById(R.id.questionDescription);
        questionDescription.setText(question.getQuestionName());
        RecyclerView recyclerView = new RecyclerView(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuestionContainerAdapter(getContext(), question.getAnswers());
    }

    public String getQuestionDescription(){
        return questionDescription.getText().toString();
    }

    public ArrayList<String> getAnswers(){
        return question.getAnswers();
    }

    public void setQuestionDescription(String text){
        questionDescription.setText(text);
    }

    public void setQuestion(Question question){
        this.question = question;
        questionDescription.setText(question.getQuestionName());
        adapter.notifyDataSetChanged();
    }
}