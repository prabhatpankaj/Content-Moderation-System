package com.techbellys.blog.service.impl;

import com.techbellys.blog.dto.BlogPostDto;
import com.techbellys.blog.entity.BlogPost;
import com.techbellys.blog.enums.Status;
import com.techbellys.blog.maper.BlogPostMapper;
import com.techbellys.blog.repository.BlogPostRepository;
import com.techbellys.blog.service.BlogPostService;
import com.techbellys.dto.AppUserDto;
import com.techbellys.utility.bedrock.service.ContentModerationByBedrockService;
import com.techbellys.utility.comprehend.service.ContentModerationByComprehendService;
import com.techbellys.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private static final Logger logger = LoggerFactory.getLogger(BlogPostServiceImpl.class);

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private BlogPostMapper blogPostMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ContentModerationByComprehendService moderationByComprehendService;

    @Autowired
    private ContentModerationByBedrockService moderateContentByClaude;

    @Override
    public BlogPostDto createBlogPost(BlogPostDto blogPostDto, Authentication authentication) {
        AppUserDto author = userUtil.extractUserFromAuth(authentication);

        BlogPost blogPost = blogPostMapper.toEntity(blogPostDto);
        blogPost.setAuthor(author);
        blogPost.setCreatedAt(LocalDateTime.now());
        blogPost.setUpdatedAt(LocalDateTime.now());
        blogPost.setStatus(Status.PENDING_MODERATION); // Set the initial status

        // Save the blog post and initiate background moderation
        BlogPost savedPost = blogPostRepository.save(blogPost);
        runModerationAsync(savedPost);

        return blogPostMapper.toDto(savedPost);
    }

    @Override
    public BlogPostDto getBlogPostById(String id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        return blogPostMapper.toDto(blogPost);
    }

    @Override
    public Page<BlogPostDto> getAllBlogPosts(Pageable pageable, Status status) {
        return blogPostRepository.findAllByStatus(status, pageable)
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

        blogPost.setUpdatedAt(LocalDateTime.now());

        if (blogPostDto.getStatus() != null && blogPostDto.getStatus() == Status.DRAFT) {
            if (blogPostDto.getTitle() != null) {
                blogPost.setTitle(blogPostDto.getTitle());
            }
            if (blogPostDto.getContent() != null) {
                blogPost.setContent(blogPostDto.getContent());
            }
            blogPost.setStatus(Status.DRAFT);
            blogPostRepository.save(blogPost);
            return blogPostMapper.toDto(blogPost);
        }

        // Update pending fields and trigger moderation
        if (blogPostDto.getTitle() != null) {
            blogPost.setPendingTitle(blogPostDto.getTitle());
        }
        if (blogPostDto.getContent() != null) {
            blogPost.setPendingContent(blogPostDto.getContent());
        }
        blogPost.setStatus(Status.PENDING_MODERATION);
        blogPostRepository.save(blogPost);

        // Run moderation in the background
        runModerationAsync(blogPost);

        return blogPostMapper.toDto(blogPost);
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

    /**
     * Runs content moderation asynchronously in the background.
     *
     * @param blogPost The blog post to moderate.
     */
    private void runModerationAsync(BlogPost blogPost) {
        CompletableFuture.runAsync(() -> {
            boolean isTitleClean = true;
            boolean isContentClean = true;

            try {
                if (blogPost.getPendingTitle() != null) {
                    isTitleClean = moderationByComprehendService.moderateContentByComprehend(blogPost.getPendingTitle())
                            .join();
                    if (isTitleClean) {
                        blogPost.setTitle(blogPost.getPendingTitle());
                        blogPost.setPendingTitle(null);
                    }
                }

                if (blogPost.getPendingContent() != null) {
                    isContentClean = moderateContentByClaude.moderateContentByClaude(blogPost.getPendingContent())
                            .join();
                    if (isContentClean) {
                        blogPost.setContent(blogPost.getPendingContent());
                        blogPost.setPendingContent(null);
                    }
                }

                if (isTitleClean && isContentClean) {
                    blogPost.setStatus(Status.APPROVED);
                } else {
                    blogPost.setStatus(Status.REJECTED);
                }

                blogPostRepository.save(blogPost);
                logger.info("Moderation completed. Final status: {}", blogPost.getStatus());
            } catch (Exception e) {
                logger.error("Error during background moderation", e);
                blogPost.setStatus(Status.REJECTED);
                blogPostRepository.save(blogPost);
            }
        });
    }
}
