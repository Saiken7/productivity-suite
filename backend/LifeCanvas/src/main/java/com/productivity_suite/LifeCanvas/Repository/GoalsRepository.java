package com.productivity_suite.LifeCanvas.Repository;

import com.productivity_suite.LifeCanvas.Entity.GoalsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalsRepository extends MongoRepository<GoalsEntity, String> {
    List<GoalsEntity> findByUserId(String userId);
    boolean existsById(String id);
    Optional<GoalsEntity>findById(String id);
}
