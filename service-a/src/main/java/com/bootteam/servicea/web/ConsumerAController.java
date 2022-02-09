package com.bootteam.servicea.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/customers")
public class ConsumerAController {

    @GetMapping()
    public List<String> customers(){
        return List.of("CUST01", "CUST02");
    }

}
