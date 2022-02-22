package com.jeremy.login_and_reg.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jeremy.login_and_reg.models.LoginUser;
import com.jeremy.login_and_reg.models.User;
import com.jeremy.login_and_reg.services.UserService;

@Controller
public class UserController {

	 @Autowired
	 private UserService userService;
	 
	 @GetMapping("/")
	 public String index(HttpSession session, Model model) {
		 if (session.getAttribute("user_id") != null) {	            
			 return "redirect:/home";
	     }	        
		 model.addAttribute("newUser", new User());	        
		 model.addAttribute("newLogin", new LoginUser());	        
		 return "index.jsp";
	 }
	 
	 @PostMapping("/register")
	 public String register(@Valid @ModelAttribute("newUser") User newUser,		
			 BindingResult result, Model model, HttpSession session) {
		 userService.register(newUser, result);
		 if (result.hasErrors()) {
			 model.addAttribute("newLogin", new LoginUser());
	         return "index.jsp";
	     }  
	        session.setAttribute("user", newUser);
	        return "redirect:/home";
	 }
	 
	    @PostMapping("/login")
	    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin,
	            BindingResult result, Model model, HttpSession session) {
	        User user = userService.login(newLogin, result);
	        if (result.hasErrors()) {
	            model.addAttribute("newUser", new User());
	            return "index.jsp";
	        }
	        session.setAttribute("user", user);
	        return "redirect:/home";
	    }

}
