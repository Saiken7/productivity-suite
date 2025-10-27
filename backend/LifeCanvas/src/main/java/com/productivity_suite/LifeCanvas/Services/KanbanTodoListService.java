package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.KanbanTodoListEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.KanbanTodoListRepository;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Responses.KanbanTodoListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class KanbanTodoListService {

    @Autowired
    private KanbanTodoListRepository kanbanTodoListRepository;

    @Autowired
    private UserRepository userRepository;

    // Get All Lists
    public List<KanbanTodoListResponse> getAllLists(String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();
        List<KanbanTodoListEntity> lists = kanbanTodoListRepository.findByUserId(userId);

        return lists.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Create a To-Do
    public void createTodoList(String title, String description, String email){

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        String userId = user.getUserId();

        KanbanTodoListEntity write = KanbanTodoListEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .title(title)
                .description(description)
                .status(KanbanTodoListEntity.Status.TODO)
                .createdAt(LocalDateTime.now())
                .build();

        kanbanTodoListRepository.save(write);
    }

    // Update Status
    public void updateStatus(String listId, Map<String, String> newStatus){
        Optional<KanbanTodoListEntity> optional = kanbanTodoListRepository.findById(listId);

        if(optional.isEmpty()){
            throw new RuntimeException("List not Found");
        }
        String status = newStatus.get("status");

        KanbanTodoListEntity lists = optional.get();
        lists.setStatus(KanbanTodoListEntity.Status.valueOf(status));

        kanbanTodoListRepository.save(lists);

    }

    // Delete To-Do
    public void deleteTodo(String id){
        kanbanTodoListRepository.deleteById(id);
    }

    // Converters
    private KanbanTodoListResponse convertToResponse(KanbanTodoListEntity entity){
        return KanbanTodoListResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }
}

