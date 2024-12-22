package com.techbellys.blog.service;

import com.techbellys.blog.dto.BlogPostDto;
import com.techbellys.blog.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BlogPostService {
    BlogPostDto createBlogPost(BlogPostDto blogPostDto, Authentication authentication);
    BlogPostDto getBlogPostById(String id);
    Page<BlogPostDto> getAllBlogPosts(Pageable pageable, Status status);
    BlogPostDto updateBlogPost(String id, BlogPostDto blogPostDto, Authentication authentication);
    void deleteBlogPost(String id, Authentication authentication);
}