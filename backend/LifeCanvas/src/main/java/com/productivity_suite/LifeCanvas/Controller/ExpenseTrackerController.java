package com.productivity_suite.LifeCanvas.Controller;


import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.ExpenseTrackerDTO;
import com.productivity_suite.LifeCanvas.Services.ExpenseTrackerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExpenseTrackerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseTrackerService expenseTrackerService;

    @GetMapping("/api/service/v2/fetch-expenses")
    public ResponseEntity<?> getAllExpenses(@Valid @RequestBody ExpenseTrackerDTO request,
                                            @CurrentSecurityContext(expression = "authentication?.name") String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        String userId = user.getUserId();

        if(userId != null){
            List<ExpenseTrackerEntity> expenses  = expenseTrackerService.getUserExpenses(userId);
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        }

        return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
    }

}
