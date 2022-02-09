package com.bootteam.serviceb.web;

import com.bootteam.serviceb.domain.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/payment")
public class ConsumerBController {

    @GetMapping()
    public ResponseEntity<String> payment(){
        return new ResponseEntity<>("payment api", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Payment> save(@RequestBody Payment payment){
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }
}
