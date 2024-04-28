package org.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("")
public class HomeController {
    @ResponseBody
    @GetMapping("")
    public String hello(){
        return "<div style='text-align: center;'><h1>Hello world</h1></div>";
    }
}
