package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@CrossOrigin(origins = "*", maxAge = 18000)
@RestController
public class OAuthAppController {
    private final UserRepository repository;

    public OAuthAppController(UserRepository repository) {
        this.repository = repository;
    }

    //Login by member account
    @PostMapping("api/loginapp")
    ResponseEntity<?> loginMember(@RequestParam String email, @RequestParam String password, HttpServletRequest httpRequest) throws NoSuchAlgorithmException {
        if (email == null || email.isEmpty()){
            return new ResponseEntity<String>(
                    String.format("Email không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (password == null || password.isEmpty()){
            return new ResponseEntity<String>(
                    String.format("Mật khẩu không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = repository.findAccountMember(email);

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        password = sb.toString();

        if (user == null || !user.getPassword().equals(password)){
            return new ResponseEntity<String>(
                    String.format("Email hoặc mật khẩu không đúng."), HttpStatus.NOT_FOUND);
        }
        if (user.getPassword().equals(password)) {
            String salt = java.time.LocalDateTime.now().toString();
            md.update(salt.getBytes());
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            String token = sb.toString();
            user.setToken(token);
            repository.save(user);

            HttpSession session = httpRequest.getSession();
            session.setAttribute("accessToken",token);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }

        return new ResponseEntity<String>(
                String.format("Email hoặc mật khẩu không đúng."), HttpStatus.NOT_FOUND);
    }
}
