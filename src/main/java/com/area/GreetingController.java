package com.area;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreetingController {
    @RequestMapping("/greeting")
    public String greeting() {
        return "Et voilà it works";
    }
}