package com.techbellys.utility.comprehend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;

@Configuration
public class AwsComprehendConfig {
    @Value("${aws.comprehend.region}")
    private String region;

    @Value("${aws.comprehend.credentials.access-key}")
    private String accessKey;

    @Value("${aws.comprehend.credentials.secret-key}")
    private String secretKey;

    @Bean
    public ComprehendClient comprehendClient() {
        return ComprehendClient.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }
}
