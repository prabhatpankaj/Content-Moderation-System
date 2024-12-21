package com.techbellys.blog.repository;

import com.techbellys.blog.entity.BlogPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogPostRepository extends MongoRepository<BlogPost, String> {
}
