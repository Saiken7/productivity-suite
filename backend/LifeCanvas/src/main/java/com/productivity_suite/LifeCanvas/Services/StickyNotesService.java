package com.productivity_suite.LifeCanvas.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivity_suite.LifeCanvas.Entity.StickyNotes;
import com.productivity_suite.LifeCanvas.Repository.StickyNoteRepository;
import com.productivity_suite.LifeCanvas.Requests.StickyNotesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StickyNotesService {

    @Autowired
    private StickyNoteRepository stickyNoteRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    public List<StickyNotes> getAllStickyNotes(String userId){
        String key = "StickyNotes:" + userId;

        Object cached = redisTemplate.opsForValue().get(key);

        if(cached != null){
            return mapper.convertValue(cached, new TypeReference<List<StickyNotes>>() {});
        }

        List<StickyNotes> response = stickyNoteRepository.findByUserIdOrderByCreatedAtDesc(userId);

        redisTemplate.opsForValue().set(key,response,Duration.ofSeconds(600l));

        return response;

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
        String key = "Note:" + noteId;
        Object cached = redisTemplate.opsForValue().get(key);

        if(cached != null){
            return mapper.convertValue(cached, StickyNotes.class);
        }

        StickyNotes note = stickyNoteRepository.findById(noteId)
                .orElseThrow(()-> new RuntimeException("Note Not Found"));

        redisTemplate.opsForValue().set(key, note, Duration.ofSeconds(600l));
        return note;
    }

    public void deleteNote(String noteId, String userId) {
        StickyNotes noteToBeDeleted = stickyNoteRepository.findById(noteId)
                .orElseThrow(()-> new RuntimeException("Note Not Found"));
        stickyNoteRepository.deleteByIdAndUserId(noteId, userId);
    }

    public void editOneNote(String noteId, StickyNotesDTO request) {
        Optional<StickyNotes> existingNoteOptional = stickyNoteRepository.findById(noteId);

        if(!existingNoteOptional.isPresent()){
            throw new RuntimeException("Note not Found");
        }

        StickyNotes existingNote = existingNoteOptional.get();

        if(existingNote.getTitle() != null){
            existingNote.setTitle(request.getTitle());
            existingNote.setCreatedAt(LocalDateTime.now());
        }

        if(existingNote.getNote() != null){
            existingNote.setNote(request.getNote());
            existingNote.setCreatedAt(LocalDateTime.now());
        }

        stickyNoteRepository.save(existingNote);

    }
}
