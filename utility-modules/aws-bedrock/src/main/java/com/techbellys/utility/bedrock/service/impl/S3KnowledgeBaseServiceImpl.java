package com.techbellys.utility.bedrock.service.impl;

import com.techbellys.utility.bedrock.knowledgebase.S3KnowledgeBase;
import com.techbellys.utility.bedrock.service.S3KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3KnowledgeBaseServiceImpl implements S3KnowledgeBaseService {

    private final S3KnowledgeBase s3KnowledgeBase;

    @Autowired
    public S3KnowledgeBaseServiceImpl(S3KnowledgeBase s3KnowledgeBase) {
        this.s3KnowledgeBase = s3KnowledgeBase;
    }

    @Override
    public String processQuery(String query) {
        return s3KnowledgeBase.process(query);
    }
}
