package com.pauloladele.ironsafe.repositories;

import com.pauloladele.ironsafe.models.Safe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafeRepository extends MongoRepository<Safe, String> {
}
