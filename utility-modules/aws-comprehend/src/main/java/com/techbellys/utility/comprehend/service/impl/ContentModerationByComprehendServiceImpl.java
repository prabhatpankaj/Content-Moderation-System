package com.techbellys.utility.comprehend.service.impl;

import com.techbellys.utility.comprehend.service.ContentModerationByComprehendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.DetectToxicContentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectToxicContentResponse;
import software.amazon.awssdk.services.comprehend.model.TextSegment;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Service
public class ContentModerationByComprehendServiceImpl implements ContentModerationByComprehendService {

    private static final Logger logger = LoggerFactory.getLogger(ContentModerationByComprehendServiceImpl.class);

    @Autowired
    private ComprehendClient comprehendClient;

    /**
     * Moderates content asynchronously using Amazon Comprehend's toxicity detection.
     *
     * @param content The content to be moderated.
     * @return A CompletableFuture containing true if the content is clean, false otherwise.
     */
    @Async
    @Override
    public CompletableFuture<Boolean> moderateContentByComprehend(String content) {
        try {
            // Create a TextSegment object with the input content
            TextSegment textSegment = TextSegment.builder()
                    .text(content)
                    .build();

            // Prepare the request with the TextSegment list and language code
            DetectToxicContentRequest request = DetectToxicContentRequest.builder()
                    .textSegments(Collections.singletonList(textSegment))
                    .languageCode("en") // Assuming the content is in English
                    .build();

            // Call Amazon Comprehend to analyze the content
            DetectToxicContentResponse response = comprehendClient.detectToxicContent(request);

            // Check for toxicity in the response
            boolean isToxic = response.resultList().stream()
                    .flatMap(result -> result.labels().stream())
                    .anyMatch(label -> label.score() > 0.5); // Adjust threshold as needed

            // Log the result for debugging
            logger.info("Content moderation result: {}", isToxic ? "Toxic" : "Clean");

            // Return a CompletableFuture with the moderation result
            return CompletableFuture.completedFuture(!isToxic);
        } catch (Exception e) {
            // Log the error
            logger.error("Error during content moderation: {}", e.getMessage(), e);

            // Return a failed CompletableFuture
            return CompletableFuture.completedFuture(false);
        }
    }
}
