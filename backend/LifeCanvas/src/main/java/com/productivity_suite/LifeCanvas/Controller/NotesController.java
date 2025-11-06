package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Requests.NotesRequest;
import com.productivity_suite.LifeCanvas.Responses.NotesResponse;
import com.productivity_suite.LifeCanvas.Services.NotesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotesController {

    private final NotesService notesService;

    @GetMapping("/api/v2/services/notes/get-user-notes")
    public ResponseEntity<?> getUserNotes(@CurrentSecurityContext(expression = "authentication?.name") String email){
        if(email == null || email == ""){
            return new ResponseEntity<>("User not Authenticated and Authorized", HttpStatus.UNAUTHORIZED);
        }
        List<NotesResponse> response = notesService.getAllNotesOfUser(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/v2/service/notes/{id}")
    public ResponseEntity<?> getNote(@PathVariable String id){
        if(id == null || id == ""){
            return new ResponseEntity<>("ID not Found", HttpStatus.BAD_REQUEST);
        }

        NotesResponse response = notesService.getNoteById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/v2/service/notes/create-note")
    public ResponseEntity<?> createNewNote(@CurrentSecurityContext(expression = "authentication?.name") String email){
        if(email == null || email == ""){
            return new ResponseEntity<>("Unauthorized to perform this action", HttpStatus.UNAUTHORIZED);
        }

        NotesResponse response = notesService.createNote(email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/api/v2/service/notes/{id}")
    public ResponseEntity<?> updateNote(@PathVariable String id,
                                        @RequestBody @Valid NotesRequest request){
        if(request.getTitle() == null || request.getContent() == null ){
            return new ResponseEntity<>("Need something to update", HttpStatus.BAD_REQUEST);
        }

        notesService.updateNote(request.getTitle(), request.getContent(), id);
        return new ResponseEntity<>("Saved", HttpStatus.OK);
    }

}
