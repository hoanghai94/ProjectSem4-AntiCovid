package com.sem4.covid.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class MessageController {

    @CrossOrigin
    @GetMapping("api/fail")
    ResponseEntity<?> getFailedMessage(HttpSession session) {
        return new ResponseEntity<String>(
                String.format("Yêu cầu đăng nhập."), HttpStatus.NOT_FOUND);
    }
}
