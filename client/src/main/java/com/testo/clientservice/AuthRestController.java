package com.testo.clientservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class AuthRestController {
    private static final Logger log = LoggerFactory.getLogger(AuthRestController.class);

    @Value("${github.clientId}")
    private String clientId;
    @Value("${github.clientSecret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final AuthState authState;

    public AuthRestController(RestTemplate restTemplate, AuthState authState) {
        this.restTemplate = restTemplate;
        this.authState = authState;
    }

    @GetMapping("/login")
    public RedirectView index() {
        String state = "iVHGWJVmY_KHJ7w4";
        String url = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("github.com")
            .path("/login/oauth/authorize")
            .queryParam("esponse_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", "http://localhost:8080/authorize")
            .queryParam("scope", "read")
            .queryParam("state", state)
            .build()
            .toUriString();

        log.info("Initial state is {}", state);
        log.info("Requesting an auth code from URL {}", url);
        return new RedirectView(url);
    }

    @GetMapping("/authorize")
    public RedirectView auth(@RequestParam String code, @RequestParam String state) {
        log.info("Auth code received is {}", code);
        log.info("State received is {}", state);

        String url = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("github.com")
            .path("/login/oauth/access_token")
            .queryParam("client_id", clientId)
            .queryParam("client_secret", clientSecret)
            .queryParam("code", code)
            .queryParam("redirect_uri", "http://localhost:8080/secured")
            .build()
            .toUriString();

        log.info("Exchanging auth code to an auth key using URL {}", url);
        ResponseEntity<OAuth2TokenResponse> auth = restTemplate.getForEntity(url, OAuth2TokenResponse.class);
        OAuth2TokenResponse body = auth.getBody();
        if (body == null) {
            log.error("Access is not granted!");
        } else {
            log.info("Access granted! {}:{} for scope '{}'", body.getTokenType(), body.getAccessToken(), body.getScope());
            authState.setClientAccess(true);
            authService(body.getAccessToken());
        }

        return new RedirectView("secured");
    }

    private void authService(String accessToken) {
        log.info("Trying to reach an external service...");
        ResponseEntity<String> auth = restTemplate.getForEntity("http://localhost:8081/check?access_token=" + accessToken, String.class);
        String response = auth.getBody();
        if (response == null) {
            log.error("Access is not granted (service)!");
        } else {
            log.info("Access granted (service)! Service says {}", response);
            authState.setServiceAccess(true);
            authState.setServiceMessage(response);
        }
    }

}
