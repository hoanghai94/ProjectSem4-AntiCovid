package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class OAuthAppController {
    private final UserRepository repository;

    public OAuthAppController(UserRepository repository) {
        this.repository = repository;
    }


    //Login by member account
    @CrossOrigin
    @GetMapping("api/member")
    String loginMember(@RequestParam String email, @RequestParam String password, HttpSession session) {
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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        if (user.getPassword().equals(password)) {
            session.setAttribute("Username", user.getUserName());
//            user.setToken();


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
