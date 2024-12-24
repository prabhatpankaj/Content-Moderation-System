package com.techbellys.utility.bedrock.aimodels;

import com.techbellys.utility.bedrock.helpers.Constants;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

/**
 * Service class to interact with the Claude model using AWS Bedrock runtime for content moderation and text generation.
 */
@Service
public class ClaudeV2 {

    private static final Logger logger = LoggerFactory.getLogger(ClaudeV2.class);
    public static final String MODEL_ID = Constants.CLAUDE_MODEL_ID_V2;

    @Autowired
    private BedrockRuntimeClient bedrockRuntimeClient;

    /**
     * Invokes the Claude model with the specified prompt and parameters.
     *
     * @param prompt      The input prompt for the Claude model.
     * @param temperature The temperature parameter for text generation.
     * @param maxTokens   The maximum number of tokens to generate.
     * @return The generated completion from the Claude model.
     */
    public String invoke(String prompt, double temperature, int maxTokens) {
        validateParameters(temperature, maxTokens);

        try {
            // Prepare the JSON body for the request
            JSONObject jsonBody = createRequestBody(prompt, temperature, maxTokens);

            logger.info("Invoking Claude model with payload: {}", jsonBody.toString());

            // Build and execute the request
            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId(MODEL_ID)
                    .body(SdkBytes.fromUtf8String(jsonBody.toString()))
                    .build();

            InvokeModelResponse response = bedrockRuntimeClient.invokeModel(request);

            logger.info("Claude model raw response: {}", response.body().asUtf8String());

            // Parse and return the response
            return parseResponse(response);
        } catch (Exception e) {
            logger.error("Error invoking Claude model. Prompt: {}, Error: {}", prompt, e.getMessage(), e);
            throw new RuntimeException("Failed to invoke Claude model", e);
        }
    }

    /**
     * Performs content moderation by analyzing the given text for abusive or negative language.
     *
     * @param text The text to analyze.
     * @return true if the content is safe, false otherwise.
     */
    public boolean moderateContent(String text) {
        String moderationPrompt = """
            You are a content moderation tool. Analyze the following text for abusive language, profanity, or negativity.
            If the content is clean and does not contain any harmful language, respond with \"true\".
            If the content is abusive, profane, or negative, respond with \"false\".

            Text: """ + text;

        try {
            String response = invoke(moderationPrompt, 0.5, 200);
            logger.info("Content moderation response: {}", response);
            return "true".equalsIgnoreCase(response.trim());
        } catch (Exception e) {
            logger.error("Content moderation failed for text: {}, Error: {}", text, e.getMessage(), e);
            throw new RuntimeException("Content moderation failed", e);
        }
    }

    /**
     * Validates input parameters for the Claude model invocation.
     *
     * @param temperature The temperature parameter for text generation.
     * @param maxTokens   The maximum number of tokens to generate.
     */
    private void validateParameters(double temperature, int maxTokens) {
        if (temperature < 0 || temperature > 1) {
            throw new IllegalArgumentException("Temperature must be between 0 and 1. Provided: " + temperature);
        }
        if (maxTokens <= 0 || maxTokens > 2048) {
            throw new IllegalArgumentException("maxTokens must be between 1 and 2048. Provided: " + maxTokens);
        }
        logger.debug("Parameters validated: temperature={}, maxTokens={}", temperature, maxTokens);
    }

    /**
     * Creates the request body for the Claude model invocation.
     *
     * @param prompt      The prompt to send to the model.
     * @param temperature The temperature for text generation.
     * @param maxTokens   The maximum tokens for the output.
     * @return A JSONObject representing the request body.
     */
    private JSONObject createRequestBody(String prompt, double temperature, int maxTokens) {
        return new JSONObject()
                .put("prompt", "Human: " + prompt + " Assistant:")
                .put("temperature", temperature)
                .put("max_tokens_to_sample", maxTokens);
    }

    /**
     * Parses the Claude model response to extract the completion.
     *
     * @param response The response from the model.
     * @return The extracted completion text.
     */
    private String parseResponse(InvokeModelResponse response) {
        return new JSONObject(response.body().asUtf8String()).getString("completion");
    }
}
