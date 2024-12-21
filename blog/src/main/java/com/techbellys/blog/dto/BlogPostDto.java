package com.techbellys.blog.dto;

import com.techbellys.blog.enums.Status;
import com.techbellys.dto.AppUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDto {
    private String id;
    private String title;         // Current approved title
    private String pendingTitle;  // New title awaiting moderation
    private String content;       // Current approved content
    private String pendingContent; // New content awaiting moderation
    private Status status;        // Moderation status
    private AppUserDto author;    // Author details
}