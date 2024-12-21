package com.techbellys.blog.entity;

import com.techbellys.blog.enums.Status;
import com.techbellys.dto.AppUserDto;
import com.techbellys.entity.MongoBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blog_posts")
public class BlogPost extends MongoBaseEntity {
    @Id
    private String id;

    @Indexed
    private String title; // Current approved title

    private String pendingTitle; // New title awaiting moderation

    private String content; // Current approved content

    private String pendingContent; // New content awaiting moderation

    @Indexed
    private AppUserDto author;

    @Indexed
    private Status status;
}
