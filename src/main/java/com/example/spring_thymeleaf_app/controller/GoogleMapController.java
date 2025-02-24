package com.example.spring_thymeleaf_app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.path("results");

            int resultCount = results.size(); // 結果の数を数える

            if (resultCount > 0) {
                Random random = new Random();
                int randomIndex = random.nextInt(resultCount); // ランダムに数字を選ぶ

                JsonNode selectedNode = results.get(randomIndex);
                String name = selectedNode.path("name").asText();
                String address = selectedNode.path("vicinity").asText();
                String placeId = selectedNode.path("place_id").asText();
                String photoReference = selectedNode.path("photos").get(0).path("photo_reference").asText();

                // PhotoリクエストURLを生成
                String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=%s&key=%s",
                                                photoReference, apiKey);

                // Google Mapsの店舗リンクを生成
                String mapUrl = String.format("https://www.google.com/maps/place/?q=place_id:%s", placeId);

                Map<String, String> item = new HashMap<>();
                item.put("name", name);
                item.put("address", address);
                item.put("photoUrl", photoUrl);
                item.put("mapUrl", mapUrl);
                model.addAttribute("result", item); // 選んだ数字番目の値だけをモデルに格納
            } else {
                model.addAttribute("result", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "result";
    }
}
