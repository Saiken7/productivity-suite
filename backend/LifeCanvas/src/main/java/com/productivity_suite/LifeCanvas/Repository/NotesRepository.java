package com.productivity_suite.LifeCanvas.Repository;

import com.productivity_suite.LifeCanvas.Entity.NotesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotesRepository extends MongoRepository<NotesEntity, String> {
    List<NotesEntity> findByUserId(String userId);
    Optional<NotesEntity> findById(String id);
}
