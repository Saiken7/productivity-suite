package com.productivity_suite.LifeCanvas.Repository;

import com.productivity_suite.LifeCanvas.Entity.CalendarEventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarEventsRepository extends MongoRepository<CalendarEventEntity, String>{

    List<CalendarEventEntity> findByUserId (String userId);
}
