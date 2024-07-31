package org.example;

import org.example.question.Question;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

    private HashMap<Long, UserData> users;
    private ArrayList<Question> questions;

    public Bot() {
        users = new HashMap<>();
        questions = new ArrayList<Question>();
        questions.add(new Question("Question 1. How much simple types are in java?",
                                    new String[] {"8"}));
        questions.add(new Question("Question 2. How much link types are in SQL?",
                new String[] {"3"}));
        questions.add(new Question("Question 3. What command in Git you can check the author of different strings in one file?",
                new String[] {"blame"}));
        questions.add(new Question("Question 4. What HTTP-request methods do you know?",
                new String[] {"get", "post", "put", "patch", "delete"}));
    }

    @Override
    public String getBotUsername() {
        return "javaTestSpecialBot";
    }

    @Override
    public String getBotToken() {
        return "6495301387:AAEDMAX7U_7ZnyK2XGeHOHfUKdF3toFufKo";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        long userId = message.getFrom().getId();

        if (text.equals("/start")) {
            sendText(userId, "Hi, it's java test. Now i'll ask you first question:");
            users.put(userId, new UserData());
            String question = getQuestion(0);
            sendText(userId, question);
        } else if (users.get(userId).getQuestionNumber() >= questions.size()) {
            sendText(userId, "You've already answered all questions.\n" +
                    "Use \"/start\" to reboot the bot.");
        } else {
            UserData userData = users.get(userId);
            boolean result = checkAnswer(userData.getQuestionNumber(), text);
            int score = userData.getScore();
            userData.setScore(score + (result ? 1 : 0));
            sendText(userId, result ? "It's right." : "It's wrong");

            if (userData.getQuestionNumber() == questions.size() - 1) {
                sendText(userId, "Your score: " + userData.getScore() +
                        " of the " + questions.size() + ".");
            }

            userData.setQuestionNumber(userData.getQuestionNumber() + 1);
            String question = getQuestion(userData.getQuestionNumber());
            sendText(userId, question);
        }
        //System.out.println(message.getText());
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm); //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); //Any error will be printed here
        }
    }

    public String getQuestion(int number) {
        if (number < questions.size()) {
            return questions.get(number).getQuestion();
        } else {
            return "";
        }
    }

    public boolean checkAnswer(int number, String answer) {
        answer = answer.toLowerCase();
        if (number < questions.size()) {
            return questions.get(number).checkAnswer(answer);
        } else {
            return false;
        }
    }
}
