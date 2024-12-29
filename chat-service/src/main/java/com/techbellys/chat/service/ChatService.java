package com.techbellys.chat.service;

import com.techbellys.chat.service.dto.QueryRequest;
import com.techbellys.chat.service.dto.QueryResponse;

public interface ChatService {
    QueryResponse processQuery(QueryRequest query);

    QueryResponse processQueryByAgent(QueryRequest query);
}
