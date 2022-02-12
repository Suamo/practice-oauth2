package com.testo.service;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceRestController {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @GetMapping("/check")
    public String check(@RequestParam("access_token") String accessToken) {
        // todo: use the token on behalf of the user
        //
        //        String url = UriComponentsBuilder.newInstance()
        //            .scheme("https")
        //            .host("api.github.com")
        //            .path("/user/" + accessToken)
        //			.queryParam("client_id", clientId)
        //            .build()
        //            .toUriString();
        //
        //        log.info("Checking GITHUB API with URL {}", url);
        //        ResponseEntity<String> auth = restTemplate.getForEntity(url, String.class);
        //        String body = auth.getBody();
        //        if (body == null) {
        //            log.error("Cannot use the token!");
        //        } else {
        //            log.info("Can use the token: {}", body);
        //        }
        //        log.info("Received token: " + accessToken);
        return "Arigato!";
    }

}
