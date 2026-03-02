package com.jdc.web2026i.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("miweb")
    public String miweb(){
        return "HolaHtml";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/pensum")
    public String pensum(){
        return "pensum";
    }

    @GetMapping("/acercade")
    public String acercade(){
        return "acercade";
    }

}
