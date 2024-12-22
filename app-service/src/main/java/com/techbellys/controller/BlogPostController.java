package com.techbellys.controller;

import com.techbellys.blog.dto.BlogPostDto;
import com.techbellys.blog.enums.Status;
import com.techbellys.blog.service.BlogPostService;
import com.techbellys.constants.ApiEndpoints;
import com.techbellys.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndpoints.BLOG)
public class BlogPostController {

    @Autowired
    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<BlogPostDto>> createBlogPost(
            @Valid @RequestBody BlogPostDto blogPostDto,
            Authentication authentication) {
        BlogPostDto createdPost = blogPostService.createBlogPost(blogPostDto, authentication);
        return ResponseEntity.ok(ApiResponse.success(createdPost));
    }

    @GetMapping(ApiEndpoints.BLOG_BY_ID)
    public ResponseEntity<ApiResponse<BlogPostDto>> getBlogPostById(@PathVariable String id) {
        BlogPostDto blogPost = blogPostService.getBlogPostById(id);
        return ResponseEntity.ok(ApiResponse.success(blogPost));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ApiResponse.PaginatedData<BlogPostDto>>> getAllBlogPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "APPROVED") Status status
    ) {
        page = Math.max(page - 1, 0); // Adjust for zero-based pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch blog posts filtered by status
        Page<BlogPostDto> blogPosts = blogPostService.getAllBlogPosts(pageable, status);

        // Return response with ApiResponse.success(Page)
        return ResponseEntity.ok(ApiResponse.success(blogPosts));
    }

    @PutMapping(ApiEndpoints.BLOG_BY_ID)
    public ResponseEntity<ApiResponse<BlogPostDto>> updateBlogPost(
            Authentication authentication,
            @PathVariable String id,
            @Valid @RequestBody BlogPostDto blogPostDto) {
        BlogPostDto updatedPost = blogPostService.updateBlogPost(id, blogPostDto, authentication);
        return ResponseEntity.ok(ApiResponse.success(updatedPost));
    }

    @DeleteMapping(ApiEndpoints.BLOG_BY_ID)
    public ResponseEntity<ApiResponse<Void>> deleteBlogPost(
            Authentication authentication,
            @PathVariable String id) {
        blogPostService.deleteBlogPost(id, authentication);
        return ResponseEntity.noContent().build();
    }
}

