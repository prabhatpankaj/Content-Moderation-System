package com.techbellys.blog.service.impl;

import com.techbellys.blog.dto.BlogPostDto;
import com.techbellys.blog.entity.BlogPost;
import com.techbellys.blog.maper.BlogPostMapper;
import com.techbellys.blog.repository.BlogPostRepository;
import com.techbellys.blog.service.BlogPostService;
import com.techbellys.dto.AppUserDto;
import com.techbellys.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private BlogPostMapper blogPostMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    public BlogPostDto createBlogPost(BlogPostDto blogPostDto, Authentication authentication) {
        AppUserDto author = userUtil.extractUserFromAuth(authentication);
        // Map the DTO to the entity
        BlogPost blogPost = blogPostMapper.toEntity(blogPostDto);
        blogPost.setAuthor(author);
        blogPost.setCreatedAt(LocalDateTime.now());
        blogPost.setUpdatedAt(LocalDateTime.now());

        // Save the blog post
        BlogPost savedPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toDto(savedPost);
    }

    @Override
    public BlogPostDto getBlogPostById(String id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        return blogPostMapper.toDto(blogPost);
    }

    @Override
    public Page<BlogPostDto> getAllBlogPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable)
                .map(blogPostMapper::toDto);
    }

    @Override
    public BlogPostDto updateBlogPost(String id, BlogPostDto blogPostDto, Authentication authentication) {
        AppUserDto author = userUtil.extractUserFromAuth(authentication);

        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        if (!blogPost.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("Unauthorized to update this blog post");
        }
        blogPost.setTitle(blogPostDto.getTitle());
        blogPost.setContent(blogPostDto.getContent());
        blogPost.setAuthor(blogPostDto.getAuthor());
        blogPost.setUpdatedAt(LocalDateTime.now());

        BlogPost updatedPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toDto(updatedPost);
    }

    @Override
    public void deleteBlogPost(String id, Authentication authentication) {
        AppUserDto author = userUtil.extractUserFromAuth(authentication);
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        if (!blogPost.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("Unauthorized to delete this blog post");
        }
        blogPostRepository.deleteById(id);
    }
}