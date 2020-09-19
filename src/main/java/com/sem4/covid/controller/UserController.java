package com.sem4.covid.controller;

import com.sem4.covid.entity.User;
import com.sem4.covid.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 18000)
@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    //Get All Users
    @GetMapping("/api/users")
    ResponseEntity<?> getAllUsers() {
        try {
            List<User> userList = repository.getAllUserActive();
            return new ResponseEntity<List>(userList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Create User
    @PostMapping("/api/user")
    ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            if (repository.checkEmailUnique(user.getEmail()).size() > 0){
                return new ResponseEntity<String>(
                        String.format("email đã tồn tại."), HttpStatus.BAD_REQUEST);
            }

            Calendar cal = Calendar.getInstance();
            user.setCreatedAt(new Timestamp(cal.getTimeInMillis()));
            user.setStatus(0);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(user.getPassword().getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            user.setPassword(sb.toString());
            repository.save(user);

            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get One User
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

    //Get chart data user
    @GetMapping("/api/user-chart")
    ResponseEntity<?> getChartDataUser(){
        try {
            ArrayList<Integer> list = new ArrayList<>();

            for(int i=0; i < 10; i++){
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,-i);
                Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
                cal.setTimeInMillis(timestamp.getTime());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                Timestamp startTime = new Timestamp(cal.getTimeInMillis());
                Timestamp endTime = new Timestamp(cal.getTimeInMillis() + 86399999);
                List<User> userList = repository.userChart(startTime,endTime);
                list.add(userList.size());
            }

            return new ResponseEntity<>(list, HttpStatus.OK);
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
