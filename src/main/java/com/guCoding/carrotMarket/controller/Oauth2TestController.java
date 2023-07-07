package com.guCoding.carrotMarket.controller;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
public class Oauth2TestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping
    public String login(){
        return "login";
    }



}
