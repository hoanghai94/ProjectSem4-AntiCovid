package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    //Get All Users
    @CrossOrigin
    @GetMapping("/api/users")
    List<User> getAllUsers() {
        return repository.getAllUserActive();
    }

    //Create User
    @CrossOrigin
    @PostMapping("/api/user")
    String createUser(@RequestBody User user){
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
            user.setStatus(0);
            repository.save(user);

            return "Register success";
        }

        return null;
    }

    //Get One User
    @CrossOrigin
    @GetMapping("/api/user/{id}")
    User getUserById(@PathVariable int id) {
        User user = repository.findIdActive(id);

        return user;
    }

    //Update User
    @CrossOrigin
    @PutMapping("/api/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable int id) {
        Calendar cal = Calendar.getInstance();
        User user = repository.findIdActive(id);
        user.setUserName(newUser.getUserName());
        user.setPhone(newUser.getPhone());
        user.setAddress(newUser.getAddress());
        user.setPassword(newUser.getPassword());
        user.setEmail(newUser.getEmail());
        user.setStatus(newUser.getStatus());
        user.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));

        return repository.save(user);
    }

    //Delete User
    @CrossOrigin
    @DeleteMapping("/api/user/{id}")
    void deleteUser(@PathVariable int id) {
        Calendar cal = Calendar.getInstance();
        User user = repository.findById(id).get();;
        user.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
        repository.save(user);
    }
}
