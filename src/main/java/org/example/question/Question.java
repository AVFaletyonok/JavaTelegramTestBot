package org.example.question;

import java.util.ArrayList;

public class Question {
    private String question;
    private String[] answers;

    public Question(String question, String[] answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public boolean checkAnswer(String answer) {
        answer = answer.toLowerCase();
        if (answers.length == 1) {
            if (!answers[0].equals(answer)) return false;
        } else {
            for (String curAnswer : answers) {
                if (!answer.contains(curAnswer)) return false;
            }
        }
        return true;
    }
}
