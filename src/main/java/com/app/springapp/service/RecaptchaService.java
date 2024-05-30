package com.app.springapp.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class RecaptchaService {
    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecret;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
            "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", recaptchaSecret);
        body.add("response", recaptchaResponse);


        Map<String, Object> response = restTemplate.postForObject(
                GOOGLE_RECAPTCHA_VERIFY_URL, body, Map.class);


        return response != null && (Boolean) response.get("success");
    }
}
