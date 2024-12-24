package com.techbellys.utility.comprehend.service;

import java.util.concurrent.CompletableFuture;

public interface ContentModerationByComprehendService {
    CompletableFuture<Boolean> moderateContentByComprehend(String content);
}
