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

    //Create admin account
    @CrossOrigin
    @PostMapping("api/admin")
    String createAdmin(@RequestBody User user){
        Calendar cal = Calendar.getInstance();
        if (user.getEmail() == null){

            return "Email is null";
        }
        if (user.getPhone() == null){

            return "Phone is null";
        }
        if (user.getPassword() == null){

            return "Password is null";
        }
        if (repository.checkEmailUnique(user.getEmail()).size()>0){

            return "Email is duplicated";
        }
        if (repository.checkPhoneUnique(user.getPhone()).size()>0){

            return "Phone is duplicated";
        }
        if (repository.checkEmailUnique(user.getEmail()).isEmpty() & repository.checkPhoneUnique(user.getPhone()).isEmpty()){
            user.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            user.setStatus(2);
            repository.save(user);

            return "Register success";
        }

        return null;
    }

    //Login by admin account
    @CrossOrigin
    @GetMapping("api/admin")
    String loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        if (email == null){

            return "Email is null";
        }
        if (password == null){

            return "Password is null";
        }
        User user = repository.findAccountAdmin(email);
        if (user == null){
            return "User invalid";
        }
        if (user.getPassword().equals(password)) {
            session.setAttribute("Username", user.getUserName());

            return "Success";
        }

        return "Fail";
    }

    //Logout
    @CrossOrigin
    @GetMapping("api/logout")
    void logout(HttpSession session) {
        session.invalidate();
    }
}
