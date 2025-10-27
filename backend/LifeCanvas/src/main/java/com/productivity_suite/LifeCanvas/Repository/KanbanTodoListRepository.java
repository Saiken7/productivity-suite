package com.productivity_suite.LifeCanvas.Repository;

import com.productivity_suite.LifeCanvas.Entity.KanbanTodoListEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface KanbanTodoListRepository extends MongoRepository<KanbanTodoListEntity, String> {
    List<KanbanTodoListEntity> findByUserId(String userId);
    Optional<KanbanTodoListEntity> findById(String id);
}
