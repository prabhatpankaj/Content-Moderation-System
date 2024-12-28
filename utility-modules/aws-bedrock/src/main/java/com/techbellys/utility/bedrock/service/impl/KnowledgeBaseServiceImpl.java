package com.techbellys.utility.bedrock.service.impl;

import com.techbellys.utility.bedrock.knowledgebase.KnowledgeBase;
import com.techbellys.utility.bedrock.service.KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    private final KnowledgeBase knowledgeBase;

    @Autowired
    public KnowledgeBaseServiceImpl(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    @Override
    public String processQuery(String knowledgeBaseId, String query) {
        return knowledgeBase.process(knowledgeBaseId, query);
    }
}
