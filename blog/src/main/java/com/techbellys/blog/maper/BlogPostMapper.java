package com.techbellys.blog.maper;

import com.techbellys.blog.dto.BlogPostDto;
import com.techbellys.blog.entity.BlogPost;
import com.techbellys.dto.AppUserDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BlogPostMapper {

    public BlogPost toEntity(BlogPostDto blogPostDto) {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(blogPostDto.getId());
        blogPost.setTitle(blogPostDto.getTitle());
        blogPost.setPendingTitle(blogPostDto.getPendingTitle());
        blogPost.setContent(blogPostDto.getContent());
        blogPost.setPendingContent(blogPostDto.getPendingContent());
        blogPost.setStatus(blogPostDto.getStatus());

        // Map author details
        if (blogPostDto.getAuthor() != null) {
            AppUserDto author = new AppUserDto();
            author.setId(blogPostDto.getAuthor().getId());
            author.setUsername(blogPostDto.getAuthor().getUsername());
            blogPost.setAuthor(author);
        }

        return blogPost;
    }

    public BlogPostDto toDto(BlogPost blogPost) {
        BlogPostDto dto = new BlogPostDto();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setPendingTitle(blogPost.getPendingTitle());
        dto.setContent(blogPost.getContent());
        dto.setPendingContent(blogPost.getPendingContent());
        dto.setStatus(blogPost.getStatus());

        // Map author details
        if (blogPost.getAuthor() != null) {
            AppUserDto author = new AppUserDto();
            author.setId(blogPost.getAuthor().getId());
            author.setUsername(blogPost.getAuthor().getUsername());
            author.setEmail(blogPost.getAuthor().getEmail());
            author.setRole(blogPost.getAuthor().getRole());
            author.setFull_name(blogPost.getAuthor().getFull_name());
            dto.setAuthor(author);
        }

        return dto;
    }
}
