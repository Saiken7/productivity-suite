package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.StickyNotes;
import com.productivity_suite.LifeCanvas.Repository.StickyNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StickyNotesService {

    @Autowired
    private StickyNoteRepository stickyNoteRepository;

    public List<StickyNotes> getAllStickyNotes(String userId){
        return stickyNoteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }



}
