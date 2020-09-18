package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 18000)
@RestController
public class OAuthAppController {
    private final UserRepository repository;

    public OAuthAppController(UserRepository repository) {
        this.repository = repository;
    }

    //Login by member account
    @PostMapping("api/loginapp")
    ResponseEntity<?> loginMember(@RequestHeader(name = "accessToken",required = true) String token, @RequestParam String email, @RequestParam String password, HttpSession session) throws NoSuchAlgorithmException {
        if (token.isEmpty() || repository.checkToken(token) == null){

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
                session.setAttribute("Username", user.getUserName());
                String salt = java.time.LocalDateTime.now().toString();
                md.update(salt.getBytes());
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

            return new ResponseEntity<User>(
                    repository.checkToken(token),HttpStatus.OK);
        }

        return null;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
