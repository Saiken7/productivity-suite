package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Entity.StickyNotes;
import com.productivity_suite.LifeCanvas.Services.StickyNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StickyNotesController {

    @Autowired
    private StickyNotesService stickyNotesService;

    @GetMapping("/api/service/v2/get-notes")
    public ResponseEntity<List<StickyNotes>> getAllNotes(String userId){
        if(userId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<StickyNotes> notes = stickyNotesService.getAllStickyNotes(userId);
        return ResponseEntity.ok(notes);
    }



}
