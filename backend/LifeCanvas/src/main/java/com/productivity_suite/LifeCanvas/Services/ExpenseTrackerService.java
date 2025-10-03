package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import com.productivity_suite.LifeCanvas.Repository.ExpenseTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseTrackerService {

    @Autowired
    private ExpenseTrackerRepository expenseTrackerRepository;

    public List<ExpenseTrackerEntity> getUserExpenses(String userId){
        return expenseTrackerRepository.findByUserId(userId);
    }


}
