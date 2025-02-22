package com.example.spring_thymeleaf_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;



@Controller
public class CatController {

    @Value("${cat.api.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/cat")
    public String getCatImage(Model model) {
  
        CatImage[] catImages = restTemplate.getForObject(url, CatImage[].class);
        if (catImages != null && catImages.length > 0) {
            model.addAttribute("catImageUrl", catImages[0].getUrl());
        }
        return "cat";
    }

    static class CatImage {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
