package com.example.cosc2657_assignment1.Answer;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return isCorrect == answer.isCorrect && Objects.equals(answerName, answer.answerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerName, isCorrect);
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
