package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.ExpenseTrackerRepository;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.ExpenseTrackerDTO;
import jakarta.transaction.Transactional;
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

    public List<ExpenseTrackerEntity> getUserExpense(String userId){
        return expenseTrackerRepository.findByUser_UserId(userId);
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
        return expenseTrackerRepository.findByUser_UserIdAndTransactionDateBetween(userId, start, end);
    }

    @Transactional
    public void deleteExpense(String id, String userId){
        if(!expenseTrackerRepository.existsByExpenseId(id)){
            throw new RuntimeException("Expense Not found or you are not permitted to delete it");
        }
        expenseTrackerRepository.deleteByExpenseIdAndUser_UserId(id,userId);
    }

}
