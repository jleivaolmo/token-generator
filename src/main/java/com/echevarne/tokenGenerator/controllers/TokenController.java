package com.echevarne.tokenGenerator.controllers;

import java.io.InputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Slf4j
public class TokenController {

	@GetMapping(value = "/getToken/{micro}")
	public ResponseEntity<String> getToken(@PathVariable("micro") String micro) {
		String jsonPath = "pj-ma-host-prod-eb47479170a2.json";
		String targetAudience = "https://" + micro + "-676620684522.europe-southwest1.run.app";
		String idToken = null;

		try {
			// Crear credenciales con alcance (scope)
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonPath);
			GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

			if (!(credentials instanceof IdTokenProvider)) {
	            throw new IllegalArgumentException("Credenciales no soportan ID tokens");
	        }

			IdTokenCredentials idTokenCredentials = IdTokenCredentials.newBuilder()
	                .setIdTokenProvider((IdTokenProvider) credentials)
	                .setTargetAudience(targetAudience)
	                .build();

	        idTokenCredentials.refresh();

	        idToken = idTokenCredentials.getAccessToken().getTokenValue();

			// Mostrar el token
			log.info("Bearer token: " + idToken);
			return new ResponseEntity<>(idToken, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

}
