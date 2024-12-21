package com.techbellys.blog.entity;

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
    private String title;

    private String content;

    @Indexed
    private AppUserDto author;
}
