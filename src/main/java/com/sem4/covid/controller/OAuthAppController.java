package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class OAuthAppController {
    private final UserRepository repository;

    public OAuthAppController(UserRepository repository) {
        this.repository = repository;
    }


    //Login by member account
    @CrossOrigin
    @PostMapping("api/member")
    String loginMember(@RequestHeader(name = "accessToken") String token,@RequestParam String email, @RequestParam String password, HttpSession session) throws NoSuchAlgorithmException {
        if (token == null){
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
                String salt = java.time.LocalDateTime.now().toString();
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(salt.getBytes());
                byte[] digest = md.digest();
                StringBuffer sb = new StringBuffer();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }

                token = sb.toString();
//                user.setToken(token);

                return token;
            }
        }
        else if (repository.checkToken(token) != null){
            session.setAttribute("Username", repository.checkToken(token).getUserName());

            return "Login Success";
        }

        return "Login Fail";
    }

    //Logout
    @CrossOrigin
    @PostMapping("api/memberlogout")
    void logout(HttpSession session) {
        session.invalidate();
    }

}
