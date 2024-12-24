package com.techbellys.utility.bedrock.service.impl;

import com.techbellys.utility.bedrock.aimodels.ClaudeV2;
import com.techbellys.utility.bedrock.service.ContentModerationByBedrockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ContentModerationByBedrockServiceImpl implements ContentModerationByBedrockService {

    @Autowired
    private ClaudeV2 claude;

    /**
     * Moderates content asynchronously using the Claude model.
     *
     * @param content The content to be moderated.
     * @return A CompletableFuture containing true if the content is clean, false otherwise.
     */
    @Async
    @Override
    public CompletableFuture<Boolean> moderateContentByClaude(String content) {
        try {
            // Use the content moderation method from ClaudeV2Service
            boolean isSafe = claude.moderateContent(content);

            // Log the result for debugging
            System.out.println("Content moderation result: " + (isSafe ? "Clean" : "Abusive"));

            // Return a CompletableFuture with the moderation result
            return CompletableFuture.completedFuture(isSafe);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error during content moderation: " + e.getMessage());

            // Return a failed CompletableFuture
            return CompletableFuture.completedFuture(false);
        }
    }
}
