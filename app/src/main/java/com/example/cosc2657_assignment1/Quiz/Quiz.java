package com.example.cosc2657_assignment1.Quiz;

import com.example.cosc2657_assignment1.Question.Question;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Quiz implements Serializable {
    @Exclude
    public static String CollectionName = "quizzes";

    @Exclude
    public static String QUIZNAME_ = "quizName";

    @Exclude
    public String qid;

    String quizName;

    ArrayList<Question> questions;

    public Quiz() {
        this.quizName = "Assignment 1";
        this.questions = new ArrayList<>();
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public boolean removeQuestion(int index){
        try{
            questions.remove(index);
        } catch (Exception e){
            System.out.println("Error removing quiz at index " + index + " " + e);
            return false;
        }
        return true;
    }

    public void addQuestion(Question question){
        if (questions == null) questions = new ArrayList<>();
        questions.add(question);
    }

    public void addQuestion(int position){
        if (questions == null) questions = new ArrayList<>();
        questions.add(position, new Question());
    }
}
