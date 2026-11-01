package com.quizapp.controller;

import com.quizapp.model.Question;
import com.quizapp.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @GetMapping("/quiz/start")
    public String startQuiz(HttpSession session, RedirectAttributes redirectAttributes) {
        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }
        try {
            session.setAttribute("questions", quizService.loadQuestions());
            session.setAttribute("currentIndex", 0);
            session.setAttribute("score", 0);
            return "redirect:/quiz";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Could not load the quiz. Please try again.");
            return "redirect:/home";
        }
    }


    @GetMapping("/quiz")
    public String showQuestion(HttpSession session, Model model) {
        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Question> questions = getQuestions(session);
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");

        if (questions == null || currentIndex == null) {
            return "redirect:/home";
        }
        if (currentIndex >= questions.size()) {
            return "redirect:/result";
        }

        model.addAttribute("question", questions.get(currentIndex));
        model.addAttribute("questionNumber", currentIndex + 1);
        model.addAttribute("totalQuestions", questions.size());
        return "quiz";
    }


    @PostMapping("/quiz/answer")
    public String submitAnswer(@RequestParam(required = false) String selectedOption,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Question> questions = getQuestions(session);
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        Integer score = (Integer) session.getAttribute("score");

        if (questions == null || currentIndex == null || score == null) {
            return "redirect:/home";
        }
        if (currentIndex >= questions.size()) {
            return "redirect:/result";
        }


        if (selectedOption == null || selectedOption.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select an answer before moving on.");
            return "redirect:/quiz";
        }

        Question current = questions.get(currentIndex);
        if (quizService.isCorrect(current, selectedOption)) {
            session.setAttribute("score", score + 1);
        }
        session.setAttribute("currentIndex", currentIndex + 1);
        return "redirect:/quiz";
    }


    @GetMapping("/result")
    public String showResult(HttpSession session, Model model) {
        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Question> questions = getQuestions(session);
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        Integer score = (Integer) session.getAttribute("score");

        if (questions == null || currentIndex == null || score == null) {
            return "redirect:/home";
        }
        if (currentIndex < questions.size()) {
            return "redirect:/quiz";
        }

        int total = questions.size();
        model.addAttribute("participantName", session.getAttribute("userName"));
        model.addAttribute("total", total);
        model.addAttribute("correct", score);
        model.addAttribute("incorrect", total - score);
        model.addAttribute("percentage", quizService.calculatePercentage(score, total));
        return "result";
    }


    private boolean isNotLoggedIn(HttpSession session) {
        return session.getAttribute("userName") == null;
    }

    @SuppressWarnings("unchecked")
    private List<Question> getQuestions(HttpSession session) {
        return (List<Question>) session.getAttribute("questions");
    }
}