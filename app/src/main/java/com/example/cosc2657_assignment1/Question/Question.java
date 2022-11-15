package com.example.cosc2657_assignment1.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    String questionName;

    ArrayList<String> answers;

    int correctAnswerIndex;

    public Question() {
        this.questionName = "";
        this.answers = new ArrayList<>();
        this.correctAnswerIndex = -1;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getCorrectAnswer(){
        if (correctAnswerIndex == -1 || answers.size() <= correctAnswerIndex) return null;
        return answers.get(correctAnswerIndex);
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
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

    public void addAnswer(String s){
        if (answers == null) answers = new ArrayList<>();
        answers.add(s);
    }
}
