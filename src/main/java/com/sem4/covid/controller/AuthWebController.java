package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("api/registerweb")
    ResponseEntity<?> createAdmin(@RequestBody User user){
        Calendar cal = Calendar.getInstance();
        if (user.getEmail() == null || user.getEmail().isEmpty()){

            return new ResponseEntity<String>(
                    String.format("Email không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()){

            return new ResponseEntity<String>(
                    String.format("Mật khẩu không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (repository.checkEmailUnique(user.getEmail()).size() > 0){

            return new ResponseEntity<String>(
                    String.format("Email đã tồn tại."), HttpStatus.BAD_REQUEST);
        }
        if (repository.checkPhoneUnique(user.getPhone()).size() > 0){

            return new ResponseEntity<String>(
                    String.format("Số điện thoại đã tồn tại."), HttpStatus.BAD_REQUEST);
        }
        if (repository.checkEmailUnique(user.getEmail()).isEmpty() & repository.checkPhoneUnique(user.getPhone()).isEmpty()){
            user.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            user.setStatus(2);
            repository.save(user);

            return new ResponseEntity<User>(
                    user, HttpStatus.OK);
        }

        return null;
    }

    //Login by admin account
    @CrossOrigin
    @PostMapping("api/loginweb")
    ResponseEntity<?> loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        if (email == null || email.isEmpty()){

            return new ResponseEntity<String>(
                    String.format("Email không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (password == null || password.isEmpty()){

            return new ResponseEntity<String>(
                    String.format("Mật khẩu không được để trống."), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User user = repository.findAccountAdmin(email);
        if (user == null){
            return new ResponseEntity<String>(
                    String.format("Email hoặc mật khẩu không đúng."), HttpStatus.NOT_FOUND);
        }
        if (user.getPassword().equals(password)) {
            session.setAttribute("Username", user.getUserName());

            return new ResponseEntity<User>(
                    user, HttpStatus.OK);
        }

        return null;
    }

    //Logout
    @CrossOrigin
    @PostMapping("api/logout")
    void logout(HttpSession session) {
        session.invalidate();
    }
}
