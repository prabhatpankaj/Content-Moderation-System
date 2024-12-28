package com.techbellys.utility.bedrock.knowledgebase;

import com.techbellys.helpers.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.Citation;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseRetrieveAndGenerateConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateResponse;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateType;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrievedReference;

@Service
public class KnowledgeBase {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBase.class);

    public static final String MODEL_ARN = Constants.CLAUDE_MODEL_ID_V2;

    @Autowired
    private BedrockAgentRuntimeClient bedrockAgentClient;

    public String process(String knowledgeBaseId, String query) {
        return invokeModelWithRAG(knowledgeBaseId, query);
    }

    private String invokeModelWithRAG(String knowledgeBaseId, String query) {
        logger.info("RAG - Invoke Knowledge Base and Model");

        try {
            // Config
            KnowledgeBaseRetrieveAndGenerateConfiguration kbConfig = KnowledgeBaseRetrieveAndGenerateConfiguration.builder()
                    .knowledgeBaseId(knowledgeBaseId)
                    .modelArn(MODEL_ARN)
                    .build();

            RetrieveAndGenerateConfiguration config = RetrieveAndGenerateConfiguration.builder()
                    .knowledgeBaseConfiguration(kbConfig)
                    .type(RetrieveAndGenerateType.KNOWLEDGE_BASE) // If not defined (Error: Invalid input provided)
                    .build();

            // Input
            RetrieveAndGenerateInput input = RetrieveAndGenerateInput.builder().text(query).build();

            RetrieveAndGenerateRequest request = RetrieveAndGenerateRequest.builder()
                    .input(input)
                    .retrieveAndGenerateConfiguration(config)
                    .build();
            // Response from Knowledge Base
            RetrieveAndGenerateResponse response = bedrockAgentClient.retrieveAndGenerate(request);
            logger.info("RAG - Response: {}", response);
            logger.info("RAG - Output: {}", response.output().text());
            logger.info("RAG - Count of Citations: {}", (response.hasCitations() ? response.citations().size() : 0));

            // Log retrieved references
            for (Citation c : response.citations()) {
                logger.info("RAG - Retrieval References: {}", (c.hasRetrievedReferences() ? c.retrievedReferences().size() : 0));

                for (RetrievedReference r : c.retrievedReferences()) {
                    logger.info("RAG - Content: {}", r.content().text());
                    logger.info("RAG - Location: {}", r.location().s3Location().uri());
                }
            }

            return response.output().text(); // Response generated from query
        } catch (Exception e) {
            logger.error("Error during RAG invocation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process query with RAG", e);
        }
    }
}
