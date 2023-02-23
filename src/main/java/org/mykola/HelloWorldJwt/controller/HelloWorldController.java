package org.mykola.HelloWorldJwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
//@Crossorigin("*")
public class HelloWorldController {

@RequestMapping(value="/hello")
    public String hello(){
    return "Hello World!!!";
}

}
