package com.example.sportsbook.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class OAuthController {

    private final DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver;
    private final AtomicReference<String> waitingState = new AtomicReference<>("waiting");
    private final AtomicReference<String> oauthResponse = new AtomicReference<>(null);

    public OAuthController(ClientRegistrationRepository clientRegistrationRepository) {
        this.authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
    }

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    // 1. Start route for OAuth2 flow
    @GetMapping("/start")
    @ResponseBody
    public String startOAuthFlow(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(request, "github");
        if (authorizationRequest != null) {
            waitingState.set("waiting");
            return "Redirect to: " + authorizationRequest.getAuthorizationRequestUri();
        }
        return "Failed to start OAuth2 flow.";
    }

    // 2. /status route. This will be polled by the client to check the status
    @GetMapping("/status")
    @ResponseBody
    public String getStatus() {
        if ("success".equals(waitingState.get())) {
            return oauthResponse.get();
        }
        return waitingState.get();
    }

    // 3. /callback route. This is the redirect URI where the OAuth2 provider will send the user after authorization
    @GetMapping("/callback")
    @ResponseBody
    public String handleCallback(@RequestParam("code") String code) {
        // Process the authorization code (exchange it for an access token)
        // For simplicity, just store the code here
        oauthResponse.set("Authorization code: " + code);
        waitingState.set("success");
        return "OAuth2 flow completed successfully.";
    }
}