package com.techbellys.blog.repository;

import com.techbellys.blog.entity.BlogPost;
import com.techbellys.blog.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogPostRepository extends MongoRepository<BlogPost, String> {
    Page<BlogPost> findAllByStatus(Status status, Pageable pageable);
}
