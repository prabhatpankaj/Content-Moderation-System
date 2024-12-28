package com.techbellys.utility.bedrock.service;

public interface S3KnowledgeBaseService {
    /**
     * Processes the user query using Bedrock Agent Runtime with RAG.
     *
     * @param query The user query.
     * @return The generated response.
     */
    String processQuery(String query);
}