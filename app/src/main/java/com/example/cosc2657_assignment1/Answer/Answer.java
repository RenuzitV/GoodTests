package com.example.cosc2657_assignment1.Answer;

import java.io.Serializable;

public class Answer implements Serializable {
    String answerName;
    boolean isCorrect;

    public Answer() {
        this.answerName = "Answer";
        this.isCorrect = false;
    }

    public Answer(Answer a) {
        this.answerName = a.getAnswerName();
        this.isCorrect = a.isCorrect();
    }

    public String getAnswerName() {
        return answerName;
    }

    public void setAnswerName(String answerName) {
        this.answerName = answerName;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
