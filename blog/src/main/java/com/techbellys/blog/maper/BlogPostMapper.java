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
        blogPost.setContent(blogPostDto.getContent());

        // Map author details
        if (blogPostDto.getAuthor() != null) {
            AppUserDto author = new AppUserDto();
            author.setId(blogPostDto.getAuthor().getId());
            author.setUsername(blogPostDto.getAuthor().getUsername());
            blogPost.setAuthor(author);
        }

        blogPost.setCreatedAt(LocalDateTime.now());
        blogPost.setUpdatedAt(LocalDateTime.now());
        return blogPost;
    }

    public BlogPostDto toDto(BlogPost blogPost) {
        BlogPostDto dto = new BlogPostDto();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setContent(blogPost.getContent());

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
