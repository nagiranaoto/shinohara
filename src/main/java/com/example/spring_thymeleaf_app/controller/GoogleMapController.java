package com.example.spring_thymeleaf_app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GoogleMapController {

    @Value("${google.api.key}")
    private String apiKey;

    @GetMapping("/search")
    public String searchNearby(
            @RequestParam(defaultValue = "35.681236") double lat,
            @RequestParam(defaultValue = "139.767125") double lng,
            @RequestParam(defaultValue = "300") int radius,
            @RequestParam(defaultValue = "チェーン") String keyword,
            Model model) {
        String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
        String url = String.format("%s?location=%f,%f&radius=%d&type=restaurant&keyword=%s&language=ja&key=%s",
                                   baseUrl, lat, lng, radius, keyword, apiKey);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        List<Map<String, String>> result = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.path("results");

            for (JsonNode node : results) {
                String name = node.path("name").asText();
                String address = node.path("vicinity").asText();
                String photoReference = node.path("photos").get(0).path("photo_reference").asText();

                // PhotoリクエストURLを生成
                String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                                                photoReference, apiKey);

                Map<String, String> item = new HashMap<>();
                item.put("name", name);
                item.put("address", address);
                item.put("photoUrl", photoUrl);
                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("result", result);
        return "result";
    }
}
