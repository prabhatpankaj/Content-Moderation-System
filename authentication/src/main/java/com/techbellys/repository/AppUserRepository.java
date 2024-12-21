package com.techbellys.repository;

import com.techbellys.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUser, String> {

    public AppUser findByUsername(String username);

    public AppUser findByEmail(String email);


}
