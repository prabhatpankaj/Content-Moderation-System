package com.techbellys.chat.service.impl;

import com.techbellys.chat.service.ChatService;
import com.techbellys.chat.service.dto.QueryRequest;
import com.techbellys.chat.service.dto.QueryResponse;
import com.techbellys.utility.bedrock.service.S3KnowledgeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private S3KnowledgeBaseService chatRAGModelWrapperService;

    @Override
    public QueryResponse processQueryUsingS3(QueryRequest query) {
        try {
            // Process the query using the service
            String response = chatRAGModelWrapperService.processQuery(query.getQuery());

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
