package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.GoalsEntity;
import com.productivity_suite.LifeCanvas.Repository.GoalsRepository;
import com.productivity_suite.LifeCanvas.Responses.GoalsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoalService {

    @Autowired
    private GoalsRepository goalsRepository;

    // GET all Goals of a User
    public List<GoalsResponse> getUserGoals(String userId){
        List<GoalsEntity> goal = goalsRepository.findByUserId(userId);
        return goal.stream()
                .map(this::convertToGoalResponse)
                .toList();
    }

    // Create a Goal
    public GoalsResponse createNewGoal(String goalTitle, LocalDate startDate, LocalDate endTime, String userId){
        GoalsEntity goal = GoalsEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .goalTitle(goalTitle)
                .goalStartTime(startDate)
                .goalEndTime(endTime)
                .status(GoalsEntity.Status.IN_PROGRESS)
                .build();

        goalsRepository.save(goal);
        return convertToGoalResponse(goal);
    }

    //Goal Completed
    public GoalsResponse updateGoal(String id){
        Optional<GoalsEntity> goal = goalsRepository.findById(id);

        if(goal.isEmpty()){
            throw new RuntimeException("Goal not Found - " +id);
        }
        GoalsEntity goals = goal.get();
        goals.setStatus(GoalsEntity.Status.COMPLETED);
        goalsRepository.save(goals);

        return convertToGoalResponse(goals);
    }



    // CONVERTERS
    private GoalsResponse convertToGoalResponse(GoalsEntity goalsEntity){
        return GoalsResponse.builder()
                .goalTitle(goalsEntity.getGoalTitle())
                .startTime(goalsEntity.getGoalStartTime())
                .endTime(goalsEntity.getGoalEndTime())
                .status(goalsEntity.getStatus())
                .build();
    }
}
