package com.productivity_suite.LifeCanvas.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.ExpenseTrackerRepository;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.ExpenseTrackerDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseTrackerService {

    @Autowired
    private ExpenseTrackerRepository expenseTrackerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper mapper;


    public List<ExpenseTrackerEntity> getUserExpense(String userId){

        Optional<UserEntity> user = userRepository.findByUserId(userId);

        if(user.isEmpty()){
            throw new RuntimeException("User not Found");
        }
        else{
            String key = "Expenses:"+userId;
            Object cached = redisTemplate.opsForValue().get(key);

            if(cached != null){
                List<ExpenseTrackerEntity> list = mapper.convertValue(cached, new TypeReference<List<ExpenseTrackerEntity>>(){});
                return list;
            }
            List<ExpenseTrackerEntity> expense =  expenseTrackerRepository.findByUser_UserId(userId);
            redisTemplate.opsForValue().set(key,expense, Duration.ofSeconds(600L));
            return expense;
        }

    }

    @Transactional
    public void addNewExpense(String email,ExpenseTrackerDTO request){

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        ExpenseTrackerEntity expense = ExpenseTrackerEntity.builder()
                .user(user)
                .expenseId(UUID.randomUUID().toString())
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .createdAt(LocalDateTime.now())
                .build();

        expenseTrackerRepository.save(expense);
    }

    public List<ExpenseTrackerEntity> getExpenseFromRange(String userId, LocalDate start, LocalDate end){

        Optional<UserEntity> user = userRepository.findByUserId(userId);

        if(user.isEmpty()){
            throw new RuntimeException("User not Found");
        }
        else{
            String key = "Expenses:Range:"+userId;
            Object cached = redisTemplate.opsForValue().get(key);

            if(cached != null){
                List<ExpenseTrackerEntity> list = mapper.convertValue(cached, new TypeReference<List<ExpenseTrackerEntity>>(){});
                return list;
            }
            List<ExpenseTrackerEntity> expense = expenseTrackerRepository
                    .findByUser_UserIdAndTransactionDateBetween(userId, start, end);
            redisTemplate.opsForValue().set(key,expense,Duration.ofSeconds(600L));
            return expense;
        }
    }

    @Transactional
    public void deleteExpense(String id, String userId){
        if(!expenseTrackerRepository.existsByExpenseId(id)){
            throw new RuntimeException("Expense Not found or you are not permitted to delete it");
        }
        expenseTrackerRepository.deleteByExpenseIdAndUser_UserId(id,userId);
    }

}
