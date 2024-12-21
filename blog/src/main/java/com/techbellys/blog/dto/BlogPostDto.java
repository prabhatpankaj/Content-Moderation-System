package com.techbellys.blog.dto;

import com.techbellys.dto.AppUserDto;
import lombok.Data;

@Data
public class BlogPostDto {
    private String id;
    private String title;
    private String content;
    private AppUserDto author;
}