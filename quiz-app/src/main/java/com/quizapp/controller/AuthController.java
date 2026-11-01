package com.quizapp.controller;

import com.quizapp.model.User;
import com.quizapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }


    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.register(fullName, email, password);
            redirectAttributes.addFlashAttribute("success", "Account created! Please login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Validation error: user ko message dikhao, bhara hua data wapas do
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "signup";
        } catch (RuntimeException e) {
            // Database wagairah ka error
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "signup";
        }
    }



    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        try {
            User user = userService.login(email, password);


            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());
            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "login";
        }
    }



    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            return "redirect:/login";
        }
        model.addAttribute("userName", userName);
        return "home";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}