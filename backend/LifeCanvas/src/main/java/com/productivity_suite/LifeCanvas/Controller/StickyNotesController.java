package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Entity.StickyNotes;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.StickyNotesDTO;
import com.productivity_suite.LifeCanvas.Services.StickyNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StickyNotesController {

    @Autowired
    private StickyNotesService stickyNotesService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/service/v2/get-notes")
    public ResponseEntity<List<StickyNotes>> getAllNotes(@CurrentSecurityContext(expression = "authentication?.name")
                                                             String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();

        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<StickyNotes> notes = stickyNotesService.getAllStickyNotes(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/api/service/v2/sticky-note/{noteId}")
    public ResponseEntity<?> getOneNote(@PathVariable String noteId){
        if(noteId != null){
          StickyNotes note = stickyNotesService.getOneNote(noteId);
            return new ResponseEntity<>(note,HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went Wrong", HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/api/service/v2/create-note")
    public ResponseEntity<?> createNote(@CurrentSecurityContext(expression = "authentication?.name") String email,
                                        @RequestBody StickyNotesDTO notesDTO){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Email not found"));

        String userId = user.getUserId();
        if(userId != null){
            stickyNotesService.createNewNote(userId, notesDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/service/v2/sticky-note/{noteId}")
    public ResponseEntity<?> deleteNote(@CurrentSecurityContext(expression = "authentication?.name")String email,
                                        @PathVariable String noteId){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Email not Found"));

        String userId = user.getUserId();

        if(userId !=null){
            stickyNotesService.deleteNote(noteId, userId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
