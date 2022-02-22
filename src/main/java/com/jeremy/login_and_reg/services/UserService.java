package com.jeremy.login_and_reg.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.jeremy.login_and_reg.models.LoginUser;
import com.jeremy.login_and_reg.models.User;
import com.jeremy.login_and_reg.repo.UserRepo;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	 // ------------------ Register ----------------- //
	public User register(User newUser, BindingResult result) {
        // TO-DO - Reject values or register if no errors:
        // Reject if email is taken (present in database) / unique validation
        if (userRepo.findByEmail(newUser.getEmail()).isPresent()) {
            result.rejectValue("email", "Unique", "This email is already in use!");
        }
        // Reject if password doesn't match confirmation
        if (!newUser.getPassword().equals(newUser.getConfirm())) {
            result.rejectValue("confirm", "Matches", "Both passwords must match!");
        }
        // Return null if result has errors
        if (result.hasErrors()) {
            return null;
            // Hash and set password, save user to database
        } else {
            String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
            newUser.setPassword(hashed);
            return userRepo.save(newUser);
        }
    }
	
	// ------------------- Login ------------------- //
	public User login(LoginUser newLogin, BindingResult result) {
        // TO-DO - Reject values:
        if (result.hasErrors()) {
            return null;
        }
        // Find user in the DB by email
        // Reject if NOT present
        Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());
        if (!potentialUser.isPresent()) {
            result.rejectValue("email", "Unique", "Unknown email!");
            return null;
        }
        // Reject if BCrypt password match fails
        User user = potentialUser.get();
        if (!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
            result.rejectValue("password", "Matches", "Invalid Password!");
        }
        // Return null if result has errors
        // Otherwise, return the user object
        if (result.hasErrors()) {
            return null;
        } else {
            return user;
        }
    }
	
	 // ------------------ Retrieve All ----------------- //
    public List<User> allUsers() {
        return userRepo.findAll();
    }

    // ------------------ Retrieve One ----------------- //
    public User findUser(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else
            return null;
    }
	
}
