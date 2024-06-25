package com.project.googlesheetsapi.config;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthorizedClientManager {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public AuthorizedClientManager(OAuth2AuthorizedClientService authorizedClientService, ClientRegistrationRepository clientRegistrationRepository) {
        this.authorizedClientService = authorizedClientService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public OAuth2AuthorizedClient getAuthorizedClient(String clientRegistrationId, String principalName) {
        return this.authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
    }
}
