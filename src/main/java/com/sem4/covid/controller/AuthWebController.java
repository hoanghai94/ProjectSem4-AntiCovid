package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Calendar;

@RestController
public class AuthWebController {
    private final UserRepository repository;

    public AuthWebController(UserRepository repository) {
        this.repository = repository;
    }

    //Create Admin account
    @CrossOrigin
    @PostMapping("api/admin")
    User createAdmin(@RequestBody User user){
        Calendar cal = Calendar.getInstance();
        user.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
        user.setStatus(2);
        repository.save(user);

        return user;
    }

    //Login by Admin account
    @CrossOrigin
    @GetMapping("api/admin")
    String loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = repository.findAccountAdmin(email);
        if (user.getPassword().equals(password)){
            session.setAttribute("Username", user.getUserName());

            return "Success";
        }else {

            return "Fail";
        }
    }

    //Logout
    @CrossOrigin
    @GetMapping("api/logout")
    String logout(HttpSession session) {
        session.invalidate();

        return null;
    }
}
