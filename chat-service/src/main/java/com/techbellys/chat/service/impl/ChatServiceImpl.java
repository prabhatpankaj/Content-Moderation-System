package com.techbellys.chat.service.impl;

import com.techbellys.chat.service.ChatService;
import com.techbellys.chat.service.dto.QueryRequest;
import com.techbellys.chat.service.dto.QueryResponse;
import com.techbellys.utility.bedrock.service.BedrockAgentService;
import com.techbellys.utility.bedrock.service.KnowledgeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Value("${knowledge_base.blog.knowledgeBaseId}")
    private String knowledgeBaseId;

    @Value("${knowledge_base.blog.agentId}")
    private String agentId;

    @Value("${knowledge_base.blog.agentAliasId}")
    private String agentAliasId;

    @Autowired
    private BedrockAgentService bedrockAgentService;

    @Override
    public QueryResponse processQuery(QueryRequest query) {
        try {
            // Process the query using the service
            String response = knowledgeBaseService.processQuery(knowledgeBaseId,query.getQuery());

            // Return the response encapsulated in QueryResponse
            QueryResponse queryResponse = new QueryResponse();
            queryResponse.setResponse(response);
            return queryResponse;
        } catch (Exception e) {
            // Log the error and rethrow an appropriate exception
            logger.error("Error processing query: {}", query.getQuery(), e);
            throw new RuntimeException("Failed to process the query.", e);
        }
    }

    @Override
    public QueryResponse processQueryByAgent(QueryRequest query) {
        try {
            // Process the query using the service
            String response = bedrockAgentService.invokeBedrockAgent(query.getQuery(), agentId, agentAliasId, UUID.randomUUID().toString());

            // Return the response encapsulated in QueryResponse
            QueryResponse queryResponse = new QueryResponse();
            queryResponse.setResponse(response);
            return queryResponse;
        } catch (Exception e) {
            // Log the error and rethrow an appropriate exception
            logger.error("Error processing query: {}", query.getQuery(), e);
            throw new RuntimeException("Failed to process the query.", e);
        }
    }

}
