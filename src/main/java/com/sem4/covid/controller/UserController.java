package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    //Get All Users
    @CrossOrigin
    @GetMapping("/api/users")
    ResponseEntity<?> getAllUsers() {
        try {
            List<User> userList = repository.getAllUserActive();
            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(userList, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Create User
    @CrossOrigin
    @PostMapping("/api/user")
    ResponseEntity<?> createUser(@Valid @RequestBody User user){
        try {
            if (repository.checkEmailUnique(user.getEmail()).size() > 0){
                return new ResponseEntity<String>(
                        String.format("email đã tồn tại."), HttpStatus.BAD_REQUEST);
            }

            Calendar cal = Calendar.getInstance();
            user.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            user.setStatus(0);
            repository.save(user);

            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get One User
    @CrossOrigin
    @GetMapping("/api/user/{id}")
    ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            User user = repository.findIdActive(id);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update User
    @CrossOrigin
    @PutMapping("/api/user/{id}")
    ResponseEntity<?> updateUser(@Valid @RequestBody User newUser, @PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();
            User user = repository.findIdActive(id);
            user.setUserName(newUser.getUserName());
            user.setPhone(newUser.getPhone());
            user.setAddress(newUser.getAddress());
            user.setPassword(newUser.getPassword());
            user.setEmail(newUser.getEmail());
            user.setStatus(newUser.getStatus());
            user.setUpdatedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(user);

            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Delete User
    @CrossOrigin
    @DeleteMapping("/api/user/{id}")
    ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            Calendar cal = Calendar.getInstance();
            User user = repository.findById(id).get();;
            user.setDeletedAt(new Timestamp(cal.getTimeInMillis()));
            repository.save(user);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
