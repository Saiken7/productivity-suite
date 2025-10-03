package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.ExpenseTrackerRepository;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.ExpenseTrackerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseTrackerService {

    @Autowired
    private ExpenseTrackerRepository expenseTrackerRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ExpenseTrackerEntity> getUserExpenses(String userId){
        return expenseTrackerRepository.findByUserId(userId);
    }

    public void addNewExpense(String email,ExpenseTrackerDTO request){

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        ExpenseTrackerEntity expense = ExpenseTrackerEntity.builder()
                .id(Long.valueOf(UUID.randomUUID().toString()))
                .user(user)
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .createdAt(LocalDateTime.now())
                .build();

    }

}
