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
    @GetMapping("/users")
    List<User> all() {

        return repository.getAll();
    }


    //Create User
    @PostMapping("/users")
    User createUser(@RequestBody User user){
        Calendar cal = Calendar.getInstance();
        user.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
        user.setStatus(0);
        repository.save(user);
        return user;
    }


    //Delete User
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable int id) {
        Calendar cal = Calendar.getInstance();
        User user = repository.findById(id).get();;
        user.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
        repository.save(user);
    }


    //Get One User
    @GetMapping("/users/{id}")
    User userById(@PathVariable int id) {
        User user = repository.findIdActive(id);
        return user ;
    }


    //Update User
    @PutMapping("/users/{id}")
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
}
