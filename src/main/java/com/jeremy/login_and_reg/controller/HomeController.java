package com.jeremy.login_and_reg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jeremy.login_and_reg.models.User;
import com.jeremy.login_and_reg.services.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		
		if (user != null) {
			User loggedUser = userService.findUser(user.getId());
			model.addAttribute("loggedUser", loggedUser);
			return "welcome.jsp";
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
