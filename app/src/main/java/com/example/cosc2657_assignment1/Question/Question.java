package com.example.cosc2657_assignment1.Question;

import com.example.cosc2657_assignment1.Answer.Answer;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    String questionName;

    ArrayList<Answer> answers;

    public Question() {
        this.questionName = "Question";
        this.answers = new ArrayList<>();
    }

    public Question(Question q) {
        this.questionName = q.getQuestionName();
        this.answers = new ArrayList<>();
        //copy answers from question
        for (Answer a : q.getAnswers()) {
            this.answers.add(new Answer(a));
        }
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    @Exclude
    public List<Answer> getCorrectAnswers(){
        List<Answer> correctAnswers = new ArrayList<>();
        for (Answer a: answers) {
            if (a.isCorrect()) {
                correctAnswers.add(a);
            }
        }
        return correctAnswers;
    }

    public boolean removeAnswer(int index){
        try{
            answers.remove(index);
        } catch (Exception e){
            System.out.println("Error removing question at index " + index + " " + e);
            return false;
        }
        return true;
    }

    public void addAnswer(Answer s){
        if (answers == null) answers = new ArrayList<>();
        answers.add(s);
    }
}
