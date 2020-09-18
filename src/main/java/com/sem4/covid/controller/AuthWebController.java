package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 18000)
@RestController
public class AuthWebController {
    private final UserRepository repository;

    public AuthWebController(UserRepository repository) {
        this.repository = repository;
    }

    //Create admin account
    @PostMapping("api/registerweb")
    ResponseEntity<?> createAdmin(@Valid @RequestBody User user) throws NoSuchAlgorithmException {
        Calendar cal = Calendar.getInstance();
        if (repository.checkEmailUnique(user.getEmail()).size() > 0){
            return new ResponseEntity<String>(
                    String.format("Email đã tồn tại."), HttpStatus.BAD_REQUEST);
        }
        if (repository.checkEmailUnique(user.getEmail()).isEmpty()){
            user.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            user.setStatus(2);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(user.getPassword().getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            user.setPassword(sb.toString());
            repository.save(user);

            return new ResponseEntity<User>(
                    user, HttpStatus.OK);
        }

        return null;
    }

    //Login by admin account
    @PostMapping("api/loginweb")
    ResponseEntity<?> loginAdmin(@Valid @RequestParam String email, @RequestParam String password) throws NoSuchAlgorithmException {
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<String>(
                    String.format("Email không để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (password == null || password.isEmpty()) {
            return new ResponseEntity<String>(
                    String.format("Mật khẩu không để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User user = repository.findAccountAdmin(email);

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        password = sb.toString();

        if (user == null){
            return new ResponseEntity<String>(
                    String.format("Email hoặc mật khẩu không đúng."), HttpStatus.NOT_FOUND);
        }
        if (user.getPassword().equals(password)) {
            String salt = java.time.LocalDateTime.now().toString();
            md.update(salt.getBytes());
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return new ResponseEntity<User>(user, HttpStatus.OK);
        }

        return new ResponseEntity<String>(
                String.format("Email hoặc mật khẩu không đúng."), HttpStatus.NOT_FOUND);
    }

    //Logout
    @PostMapping("api/logout")
    void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        session.invalidate();
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
