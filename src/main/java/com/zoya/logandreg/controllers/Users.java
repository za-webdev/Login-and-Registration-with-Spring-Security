package com.zoya.logandreg.controllers;

import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zoya.logandreg.models.User;
import com.zoya.logandreg.services.UserService;
import com.zoya.logandreg.validator.UserValidator;

@Controller
public class Users {
	private UserService userService;
	 private UserValidator userValidator;
    
    public Users(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }
	
	@RequestMapping("/registration")
    public String registerForm(@Valid @ModelAttribute("user") User user) {
        return "reg.jsp";
    }
	
	@PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session){
		userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "reg.jsp";
        }
        ArrayList<User> allUsers = userService.all();
		if (allUsers.size() == 0) {
			userService.saveWithUserRole(user);
			userService.addAdminRole(user);
		}
		else {
		userService.saveWithUserRole(user);
		}
		return "redirect:/login";
    }
    
    @RequestMapping("/login")
    public String login(@RequestParam(value="error", required=false) String error, @RequestParam(value="logout", required=false) String logout, Model model) {
        if(error != null) {
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
            return "login.jsp";
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successful!");
            return "login.jsp";
        }
        return "login.jsp";
    }
    
    @RequestMapping("/admin")
    public String adminPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("currentUser", userService.findByUsername(username));
        return "adminPage.jsp";
    }
    
    @RequestMapping(value= {"/","/home"})
    public String home(Principal principal, Model model) {
   
        String username = principal.getName();
        model.addAttribute("currentUser", userService.findByUsername(username));
        return "home.jsp";
    }
}
