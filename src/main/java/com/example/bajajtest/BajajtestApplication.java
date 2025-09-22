package com.example.bajajtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BajajtestApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BajajtestApplication.class, args);
	}

	@Override
	public void run(String... args) {
		RestTemplate restTemplate = new RestTemplate();

		// Step 1: Generate webhook
		String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "Your Name");       // 👉 apna naam daalo
		requestBody.put("regNo", "0105CS221102");   // 👉 apna registration number
		requestBody.put("email", "your@email.com"); // 👉 apna email

		ResponseEntity<Map> response = restTemplate.postForEntity(generateUrl, requestBody, Map.class);
		Map<String, Object> responseMap = response.getBody();

		if (responseMap == null) {
			System.out.println("Failed to generate webhook");
			return;
		}

		String webhookUrl = (String) responseMap.get("webhook");
		String accessToken = (String) responseMap.get("accessToken");

		System.out.println("Webhook URL: " + webhookUrl);
		System.out.println("AccessToken: " + accessToken);

		// Step 2: SQL query (Question 2 ka correct query yaha daalna hai)
		String finalQuery = "SELECT * FROM employees;"; // 👉 Apna SQL solution daalo

		// Step 3: Send final query
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", accessToken);

		Map<String, String> finalBody = new HashMap<>();
		finalBody.put("finalQuery", finalQuery);

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(finalBody, headers);
		ResponseEntity<String> submitResponse = restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);

		System.out.println("Submit response: " + submitResponse.getBody());
	}
}

