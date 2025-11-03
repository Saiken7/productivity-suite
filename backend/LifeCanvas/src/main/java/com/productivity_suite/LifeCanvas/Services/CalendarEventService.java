package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.CalendarEventEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.CalendarEventsRepository;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Responses.CalendarEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CalendarEventService {

    @Autowired
    private CalendarEventsRepository calendarEventsRepository;

    @Autowired
    private UserRepository userRepository;

    // Get Calendar Events of User
    public List<CalendarEventResponse> getUserEvents(String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();
        List<CalendarEventEntity> response = calendarEventsRepository.findByUserId(userId);

        return response.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Create event
    public void createCalendarEvent(String title, boolean allDay, LocalDateTime start, LocalDateTime end, String email){

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();

        if(title == null || start == null){
            throw new RuntimeException("Contents missing");
        }

        if (allDay || end == null) {
            allDay = true;
            end = start.withHour(23).withMinute(59).withSecond(59);
        } else {
            if (end.isBefore(start)) {
                throw new RuntimeException("End time cannot be before start time");
            }
        }

        CalendarEventEntity event = CalendarEventEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .title(title)
                .allDay(allDay)
                .start(start)
                .end(end)
                .createdAt(LocalDateTime.now())
                .build();

        calendarEventsRepository.save(event);
    }

    // Delete Event
    public void deleteCalendarEvent(String id){
        if(id == null || id == ""){
            throw new RuntimeException("ID not available");
        }
        calendarEventsRepository.deleteById(id);
    }

    // Converters
    private CalendarEventResponse convertToResponse(CalendarEventEntity entity){
        return CalendarEventResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .allDay(entity.isAllDay())
                .start(entity.getStart())
                .end(entity.getEnd())
                .build();
    }

}
