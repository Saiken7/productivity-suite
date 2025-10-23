package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Entity.GoalsEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.GoalRequest;
import com.productivity_suite.LifeCanvas.Responses.GoalsResponse;
import com.productivity_suite.LifeCanvas.Services.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoalController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoalService goalService;

    @GetMapping("/api/services/v2/goals")
    public ResponseEntity<?> getAllGoalsOfUser(@CurrentSecurityContext(expression = "authentication?.name") String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();

        if(userId != null){
            List<GoalsResponse> response = goalService.getUserGoals(userId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        return new ResponseEntity<>("Something went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/api/services/v2/create-goals")
    public ResponseEntity<?> createGoals(@RequestBody @Valid GoalRequest request,
                                         @CurrentSecurityContext(expression = "authentication?.name")String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();
        if(request.getGoalTitle() == null || request.getStartTime() == null || request.getEndTime() == null){
            return new ResponseEntity<>("Enter Proper Values",HttpStatus.BAD_REQUEST);
        }
        GoalsResponse response = goalService.createNewGoal(request.getGoalTitle(), request.getStartTime(), request.getEndTime(), userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/api/services/v2/update-goal/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable String id){
        if(id == null){
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.BAD_REQUEST);
        }
        GoalsResponse response = goalService.updateGoal(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
