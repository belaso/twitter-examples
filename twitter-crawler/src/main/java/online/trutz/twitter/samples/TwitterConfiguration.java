package online.trutz.twitter.samples;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@Configuration
class TwitterConfiguration {

	private static final String REGISTRATION_ID = "twitter";

	private static final Logger log = LoggerFactory.getLogger(TwitterConfiguration.class);

	@Bean
	ClientRegistration clientRegistration(
			@Value("${spring.security.oauth2.client.registration.twitter.client-id}") String clientId,
			@Value("${spring.security.oauth2.client.registration.twitter.client-secret}") String clientSecret) {
		final var TOKEN_URI = "https://api.twitter.com/oauth2/token";
		return ClientRegistration.withRegistrationId(REGISTRATION_ID).tokenUri(TOKEN_URI).clientId(clientId)
				.clientSecret(clientSecret).authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).build();
	}

	@Bean
	ClientRegistrationRepository clientRegistrationRepository(ClientRegistration clientRegistration) {
		return new InMemoryClientRegistrationRepository(clientRegistration);
	}

	@Bean
	OAuth2AuthorizedClientService auth2AuthorizedClientService(
			ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	@Bean
	AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientService authorizedClientService) {
		OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials().build();
		AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientService);
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
		return authorizedClientManager;
	}

	@Bean
	OAuth2AccessToken accessToken(
			AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager) {
		var authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(REGISTRATION_ID)
				.principal("Twitter Samples").build();
		var authorizedClient = authorizedClientServiceAndManager.authorize(authorizeRequest);
		var accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
		log.info("Access token issued:{}, expires:{}", accessToken.getIssuedAt(), accessToken.getExpiresAt());
		return accessToken;
	}

}
