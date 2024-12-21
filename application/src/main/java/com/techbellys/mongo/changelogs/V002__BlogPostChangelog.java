package com.techbellys.mongo.changelogs;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.ArrayList;

@ChangeUnit(order = "002", id = "blogpost-indexes", author = "prabhat")
public class V002__BlogPostChangelog {

    @Execution
    public void createBlogPostIndexes(MongoDatabase mongoDatabase) {
        var collection = mongoDatabase.getCollection("blog_posts");

        // Handle index for "title"
        boolean titleIndexExists = collection.listIndexes()
                .into(new ArrayList<>())
                .stream()
                .anyMatch(index -> index.get("key").toString().contains("title"));

        if (!titleIndexExists) {
            collection.createIndex(Indexes.ascending("title"), new IndexOptions().unique(false));
            System.out.println("Created index on title for BlogPost");
        } else {
            System.out.println("Index on title already exists for BlogPost");
        }

        // Handle index for "author.id"
        boolean authorIdIndexExists = collection.listIndexes()
                .into(new ArrayList<>())
                .stream()
                .anyMatch(index -> index.get("key").toString().contains("author.id"));

        if (!authorIdIndexExists) {
            collection.createIndex(Indexes.ascending("author.id"), new IndexOptions().unique(false));
            System.out.println("Created index on author.id for BlogPost");
        } else {
            System.out.println("Index on author.id already exists for BlogPost");
        }
    }

    @RollbackExecution
    public void rollbackBlogPostIndexes(MongoDatabase mongoDatabase) {
        var collection = mongoDatabase.getCollection("blog_posts");

        try {
            collection.listIndexes()
                    .into(new ArrayList<>())
                    .stream()
                    .filter(index -> index.get("key").toString().contains("title"))
                    .findFirst()
                    .ifPresent(index -> {
                        collection.dropIndex(index.get("name").toString());
                        System.out.println("Rolled back index on title for BlogPost");
                    });
        } catch (Exception e) {
            System.out.println("No index to rollback for title in BlogPost: " + e.getMessage());
        }

        try {
            collection.listIndexes()
                    .into(new ArrayList<>())
                    .stream()
                    .filter(index -> index.get("key").toString().contains("author.id"))
                    .findFirst()
                    .ifPresent(index -> {
                        collection.dropIndex(index.get("name").toString());
                        System.out.println("Rolled back index on author.id for BlogPost");
                    });
        } catch (Exception e) {
            System.out.println("No index to rollback for author.id in BlogPost: " + e.getMessage());
        }
    }
}
