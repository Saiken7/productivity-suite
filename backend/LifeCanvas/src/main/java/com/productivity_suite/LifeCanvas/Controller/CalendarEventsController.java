package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Requests.CalendarEventsRequest;
import com.productivity_suite.LifeCanvas.Responses.CalendarEventResponse;
import com.productivity_suite.LifeCanvas.Services.CalendarEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CalendarEventsController {

    @Autowired
    private CalendarEventService calendarEventService;

    @GetMapping("/api/services/v2/get-events")
    public ResponseEntity<?> getUserEvents (@CurrentSecurityContext(expression = "authentication?.name") String email){
        if(email == "null" || email == "" ){
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.UNAUTHORIZED);
        }
        List<CalendarEventResponse> response = calendarEventService.getUserEvents(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/service/v2/add-event")
    public ResponseEntity<String> createEvent(@CurrentSecurityContext(expression = "authentication?.name") String email,
                                              @Valid @RequestBody CalendarEventsRequest request){
        if(email == null || email == ""){
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.UNAUTHORIZED);
        }

        if(request.getTitle() == null || request.getStart() == null){
            return new ResponseEntity<>("Contents Missing", HttpStatus.BAD_REQUEST);
        }

        calendarEventService.createCalendarEvent(request.getTitle(), request.isAllDay(),
                request.getStart(), request.getEnd(),email);
        return new ResponseEntity<>("Event Added", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/service/v2/delete-event/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable String id){
        if(id==null){
            return new ResponseEntity<>("Event not Found", HttpStatus.BAD_REQUEST);
        }

        calendarEventService.deleteCalendarEvent(id);
        return new ResponseEntity<>("Event Deleted", HttpStatus.OK);
    }
}
