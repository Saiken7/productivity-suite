package com.productivity_suite.LifeCanvas.Repository;

import com.productivity_suite.LifeCanvas.Entity.StickyNotes;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StickyNoteRepository extends MongoRepository<StickyNotes, String> {
    List<StickyNotes> findByUserId(String userId);

    // Newest to Oldest
    List<StickyNotes> findByUserIdOrderByCreatedAtDesc(String userId);

}
