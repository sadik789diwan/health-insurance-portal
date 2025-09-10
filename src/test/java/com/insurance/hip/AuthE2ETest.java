package com.insurance.hip;

import com.insurance.hip.dto.AuthRequest;
import com.insurance.hip.dto.AuthResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void loginAndCallProtected() {
        // Step 1: Login with known user
        AuthRequest req = new AuthRequest("alice", "password");
        ResponseEntity<AuthResponse> loginResp =
                restTemplate.postForEntity("/auth/login", req, AuthResponse.class);

        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        Assertions.assertNotNull(loginResp.getBody());

        String token = loginResp.getBody().getAccessToken();
        Assertions.assertNotNull(token);

        // Step 2: Call protected endpoint with token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> profileResp =
                restTemplate.exchange("/api/user/profile", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, profileResp.getStatusCode());
        Assertions.assertTrue(profileResp.getBody().contains("User profile"));
    }
}
