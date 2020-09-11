package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("api/loginapp")
    ResponseEntity<?> loginMember(@RequestHeader(name = "accessToken",required = true) String token, @RequestParam String email, @RequestParam String password, HttpSession session) throws NoSuchAlgorithmException {
        if (token.isEmpty() || repository.checkToken(token) == null){
            if (email == null || email.isEmpty()){

                return new ResponseEntity<String>(
                        String.format("Email không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (password == null || password.isEmpty()){

                return new ResponseEntity<String>(
                        String.format("Mật khẩu không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
            }
            User user = repository.findAccountMember(email);
            if (user == null || !user.getPassword().equals(password)){
                return new ResponseEntity<String>(
                        String.format("Email hoặc mật khẩu không đúng."), HttpStatus.NOT_FOUND);
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
                user.setToken(token);
                repository.save(user);

                return new ResponseEntity<User>(
                        user, HttpStatus.OK);
            }
        }
        if (repository.checkToken(token) != null){
            session.setAttribute("Username", repository.checkToken(token).getUserName());

            return new ResponseEntity<User>(
                    repository.checkToken(token),HttpStatus.OK);
        }

        return null;
    }

}
