package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Requests.KanbanTodoListRequest;
import com.productivity_suite.LifeCanvas.Responses.KanbanTodoListResponse;
import com.productivity_suite.LifeCanvas.Services.KanbanTodoListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class KanbanTodoListController {

    @Autowired
    private KanbanTodoListService kanbanTodoListService;

    @GetMapping("/api/services/v2/todo-list")
    public ResponseEntity<?> getAllLists (@CurrentSecurityContext(expression = "authentication?.name")String email){
        if(email == null || email == ""){
            return new ResponseEntity<>("User Not Authenticated or Authorized", HttpStatus.BAD_REQUEST);
        }
        List<KanbanTodoListResponse> response = kanbanTodoListService.getAllLists(email);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/services/v2/todo-list")
    public ResponseEntity<String> createTodo(@CurrentSecurityContext(expression = "authentication?.name")String email,
                                             @Valid @RequestBody KanbanTodoListRequest request){
        if(email == null || email == ""){
            if(request == null){
                return new ResponseEntity<>("Something Went Wrong", HttpStatus.BAD_REQUEST);
            }
        }
        kanbanTodoListService.createTodoList(request.getTitle(), request.getDescription(), email);
        return new ResponseEntity<>("List Added", HttpStatus.CREATED);
    }

    @PatchMapping("/api/services/v2/todo-list/{id}")
    public ResponseEntity<String> patchStatus(@PathVariable String id, @RequestBody Map<String, String> newStatus){
        if(id == null || id == ""){
            return new ResponseEntity<>("List Not Found", HttpStatus.BAD_REQUEST);
        }
        kanbanTodoListService.updateStatus(id,newStatus);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @DeleteMapping("/api/services/v2/todo-list/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable String id){
        if(id==null || id==""){
            return new ResponseEntity<>("ID Not Found", HttpStatus.BAD_REQUEST);
        }
        kanbanTodoListService.deleteTodo(id);
        return new ResponseEntity<>("Todo Deleted", HttpStatus.OK);

    }
}
