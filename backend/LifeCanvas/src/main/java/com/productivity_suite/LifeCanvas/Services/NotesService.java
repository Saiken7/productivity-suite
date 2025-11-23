package com.productivity_suite.LifeCanvas.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivity_suite.LifeCanvas.Entity.NotesEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.NotesRepository;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Responses.NotesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    // Get all Notes of user
    public List<NotesResponse> getAllNotesOfUser(String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Authenticated"));

        String userId = user.getUserId();
        String key = "Notes:"+userId;

        Object cached = redisTemplate.opsForValue().get(key);

        if(cached != null){
            List<NotesEntity> note = mapper.convertValue(cached, new TypeReference<List<NotesEntity>>() {});
            return note.stream()
                    .map(this::convertToNoteResponse)
                    .toList();
        }

        List<NotesEntity> response = notesRepository.findByUserId(userId);

        redisTemplate.opsForValue().set(key,response, Duration.ofSeconds(600L));

        return response.stream()
                .map(this::convertToNoteResponse)
                .toList();
    }

    // Get a note by Id
    public NotesResponse getNoteById(String id){
        Optional<NotesEntity> optional = notesRepository.findById(id);

        if(optional.isEmpty()){
            throw new RuntimeException("Note Not Found");
        }

        NotesEntity note = optional.get();
        String key = "SingleNote:" + id;

        Object cached = redisTemplate.opsForValue().get(key);

        if(cached != null){
            NotesEntity response = mapper.convertValue(cached, NotesEntity.class);
            return convertToNoteResponse(response);
        }

        redisTemplate.opsForValue().set(key,note, Duration.ofSeconds(600L));
        return convertToNoteResponse(note);
    }

    // create a note
    public NotesResponse createNote(String email){

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Email not authenticated"));

        String userId = user.getUserId();

        Map<String, Object> emptyContent = Map.of(
                "type", "doc",
                "content", List.of()
        );

        NotesEntity note = NotesEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .title("Untitled")
                .content(emptyContent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        notesRepository.save(note);

        return convertToNoteResponse(note);
    }

    // update note
    public void updateNote(String title, Map<String, Object> content, String id){
        Optional<NotesEntity> existingNote = notesRepository.findById(id);
        if(existingNote.isEmpty()){
            throw new RuntimeException("Note not Found");
        }

        NotesEntity note = existingNote.get();

        note.setTitle(title);
        note.setContent(content);
        note.setUpdatedAt(LocalDateTime.now());

        notesRepository.save(note);
    }


    // Converters
    private NotesResponse convertToNoteResponse (NotesEntity entity){
        return NotesResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }
}
