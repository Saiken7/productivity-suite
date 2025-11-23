package com.productivity_suite.LifeCanvas.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivity_suite.LifeCanvas.Entity.GoalsEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.GoalsRepository;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Responses.GoalsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoalService {

    @Autowired
    private GoalsRepository goalsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper mapper;


    // GET all Goals of a User
    public List<GoalsResponse> getUserGoals(String userId){

        Optional<UserEntity> exitsUser = userRepository.findByUserId(userId);

        if(exitsUser.isEmpty()){
            throw new RuntimeException("User Not Found");
        }else{
            String key = "Goals:"+userId;
            Object cached = redisTemplate.opsForValue().get(key);

            if(cached != null){
                List<GoalsEntity> list = mapper.convertValue(cached, new TypeReference<List<GoalsEntity>>() {});
                return list.stream()
                        .map(this::convertToGoalResponse)
                        .toList();
            }

            List<GoalsEntity> goal = goalsRepository.findByUserId(userId);
            redisTemplate.opsForValue().set(key,goal, Duration.ofSeconds(600L));
            return goal.stream()
                    .map(this::convertToGoalResponse)
                    .toList();
        }


    }

    //GET selected goal of a user
    public GoalsResponse getGoal(String id){
        Optional<GoalsEntity> optional = goalsRepository.findById(id);

        if(optional.isEmpty()){
            throw new RuntimeException("Goal Not Found");
        }else{
            String key = "Goal:"+id;
            Object cached = redisTemplate.opsForValue().get(key);

            if(cached != null){
                GoalsEntity response = mapper.convertValue(cached,GoalsEntity.class);
                return convertToGoalResponse(response);
            }

            GoalsEntity goal = optional.get();
            redisTemplate.opsForValue().set(key,goal, Duration.ofSeconds(600L));
            return convertToGoalResponse(goal);
        }

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
    public GoalsResponse markComplete(String id){
        Optional<GoalsEntity> goal = goalsRepository.findById(id);

        if(goal.isEmpty()){
            throw new RuntimeException("Goal not Found - " +id);
        }
        GoalsEntity goals = goal.get();
        goals.setStatus(GoalsEntity.Status.COMPLETED);
        goalsRepository.save(goals);

        return convertToGoalResponse(goals);
    }

    // Edit Goal
    public GoalsResponse updateGoal(String id, String goalTitle, LocalDate startDate, LocalDate endDate){
        Optional<GoalsEntity> goal = goalsRepository.findById(id);
        if(goal.isEmpty()){
            throw new RuntimeException("Goal not Found - "+ id);
        }
        GoalsEntity goals = goal.get();
        goals.setGoalTitle(goalTitle);
        goals.setGoalStartTime(startDate);
        goals.setGoalEndTime(endDate);

        goalsRepository.save(goals);
        return convertToGoalResponse(goals);
    }

    public void deleteUserGoal(String id){
        Optional<GoalsEntity> goal = goalsRepository.findById(id);
        if(goal.isEmpty()){
            throw new RuntimeException("Goal Does not Exist ");
        }
        goalsRepository.deleteById(id);
    }



    // CONVERTERS
    private GoalsResponse convertToGoalResponse(GoalsEntity goalsEntity){
        return GoalsResponse.builder()
                .id(goalsEntity.getId())
                .goalTitle(goalsEntity.getGoalTitle())
                .startTime(goalsEntity.getGoalStartTime())
                .endTime(goalsEntity.getGoalEndTime())
                .status(goalsEntity.getStatus())
                .build();
    }
}
