package com.techbellys.mongo.changelogs;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.ArrayList;

@ChangeUnit(order = "001", id = "appuser-indexes", author = "prabhat")
public class V001__AppUserChangelog {

    @Execution
    public void createAppUserIndexes(MongoDatabase mongoDatabase) {
        var collection = mongoDatabase.getCollection("user");

        // Handle index for "username"
        boolean usernameIndexExists = collection.listIndexes()
                .into(new ArrayList<>())
                .stream()
                .anyMatch(index -> index.get("key").toString().contains("username"));

        if (!usernameIndexExists) {
            collection.createIndex(Indexes.ascending("username"), new IndexOptions().unique(true));
            System.out.println("Created unique index on username for AppUser");
        } else {
            System.out.println("Index on username already exists for AppUser");
        }

        // Handle index for "email"
        boolean emailIndexExists = collection.listIndexes()
                .into(new ArrayList<>())
                .stream()
                .anyMatch(index -> index.get("key").toString().contains("email"));

        if (!emailIndexExists) {
            collection.createIndex(Indexes.ascending("email"), new IndexOptions().unique(true));
            System.out.println("Created unique index on email for AppUser");
        } else {
            System.out.println("Index on email already exists for AppUser");
        }
    }

    @RollbackExecution
    public void rollbackAppUserIndexes(MongoDatabase mongoDatabase) {
        var collection = mongoDatabase.getCollection("user");

        try {
            collection.listIndexes()
                    .into(new ArrayList<>())
                    .stream()
                    .filter(index -> index.get("key").toString().contains("username"))
                    .findFirst()
                    .ifPresent(index -> {
                        collection.dropIndex(index.get("username").toString());
                        System.out.println("Rolled back unique index on username for AppUser");
                    });
        } catch (Exception e) {
            System.out.println("No index to rollback for username in AppUser: " + e.getMessage());
        }

        try {
            collection.listIndexes()
                    .into(new ArrayList<>())
                    .stream()
                    .filter(index -> index.get("key").toString().contains("email"))
                    .findFirst()
                    .ifPresent(index -> {
                        collection.dropIndex(index.get("username").toString());
                        System.out.println("Rolled back unique index on email for AppUser");
                    });
        } catch (Exception e) {
            System.out.println("No index to rollback for email in AppUser: " + e.getMessage());
        }
    }
}
