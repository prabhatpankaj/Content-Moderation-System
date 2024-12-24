package com.techbellys.utility.bedrock.service;

import java.util.concurrent.CompletableFuture;

public interface ContentModerationByBedrockService {
    CompletableFuture<Boolean> moderateContentByClaude(String content);
}
