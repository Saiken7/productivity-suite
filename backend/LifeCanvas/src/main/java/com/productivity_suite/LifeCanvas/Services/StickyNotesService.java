package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.StickyNotes;
import com.productivity_suite.LifeCanvas.Repository.StickyNoteRepository;
import com.productivity_suite.LifeCanvas.Requests.StickyNotesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class StickyNotesService {

    @Autowired
    private StickyNoteRepository stickyNoteRepository;

    public List<StickyNotes> getAllStickyNotes(String userId){
        return stickyNoteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }


    public void createNewNote(String userId, StickyNotesDTO notesDTO) {
        StickyNotes newNote =  StickyNotes.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .title(notesDTO.getTitle())
                .note(notesDTO.getNote())
                .createdAt(LocalDateTime.now())
                .build();

        stickyNoteRepository.save(newNote);
    }

    public StickyNotes getOneNote(String noteId) {
       return stickyNoteRepository.findById(noteId)
               .orElseThrow(()-> new RuntimeException("Note not Found"));
    }

    public void deleteNote(String noteId, String userId) {
        StickyNotes noteToBeDeleted = stickyNoteRepository.findById(noteId)
                .orElseThrow(()-> new RuntimeException("Note Not Found"));
        stickyNoteRepository.deleteByIdAndUserId(noteId, userId);
    }
}
