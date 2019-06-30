package com.shouldyou.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class GoogleSearchController {

	private static final Logger LOG = LoggerFactory.getLogger(GoogleSearchController.class);
	
	@Value("${searchurl}")
	String searchUrl;
	
	@GetMapping("/proxy")
	public ResponseEntity<String> proxySearchRequest(
			@RequestParam("term") String searchTerm) {
		try {
			searchTerm = searchTerm.replaceAll(" ", "%20");
			URL url = new URL(searchUrl+searchTerm);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String resLine;
			StringBuffer response = new StringBuffer();
			
			while((resLine = in.readLine()) != null) {
				response.append(resLine);
			}
			in.close();
			return ResponseEntity.ok(response.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}
