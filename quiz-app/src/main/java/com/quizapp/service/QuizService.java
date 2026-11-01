package com.quizapp.service;

import com.quizapp.model.Question;
import com.quizapp.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuestionRepository questionRepository;

    public QuizService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    public List<Question> loadQuestions() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            throw new IllegalStateException("No questions available right now.");
        }
        return questions;
    }


    public boolean isCorrect(Question question, String selectedOption) {
        return question.getCorrectOption().equalsIgnoreCase(selectedOption);
    }


    public double calculatePercentage(int correct, int total) {
        if (total == 0) {
            return 0;
        }
        return Math.round(correct * 10000.0 / total) / 100.0;
    }
}