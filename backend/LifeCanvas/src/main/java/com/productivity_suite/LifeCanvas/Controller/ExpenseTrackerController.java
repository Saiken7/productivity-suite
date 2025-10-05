package com.productivity_suite.LifeCanvas.Controller;


import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.ExpenseRangeDTO;
import com.productivity_suite.LifeCanvas.Requests.ExpenseTrackerDTO;
import com.productivity_suite.LifeCanvas.Services.ExpenseTrackerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ExpenseTrackerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseTrackerService expenseTrackerService;

    @GetMapping("/api/service/v2/fetch-expenses")
    public ResponseEntity<?> getAllExpenses(@CurrentSecurityContext(expression = "authentication?.name") String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        String userId = user.getUserId();

        if(userId != null || userId != ""){
            List<ExpenseTrackerEntity> expenses  = expenseTrackerService.getUserExpense(userId);
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        }

        return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/service/v2/get-expense-range")
    public ResponseEntity<?> getExpenseFromARange(@CurrentSecurityContext(expression = "authentication?.name") String email,
                                                  @RequestBody ExpenseRangeDTO request){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();

        if(userId != null || userId != ""){
            List<ExpenseTrackerEntity> expensesRange = expenseTrackerService.getExpenseFromRange(userId, request.getStart(), request.getEnd());
            return new ResponseEntity<>(expensesRange, HttpStatus.OK);
        }
        return new ResponseEntity<>("Something Went Wrong" , HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/service/v2/add-expense")
    public ResponseEntity<?> addExpense(@Valid @RequestBody ExpenseTrackerDTO request,
                                        @CurrentSecurityContext(expression = "authentication?.name")String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        if(user != null){
            expenseTrackerService.addNewExpense(email, request);
            return new ResponseEntity<>("Expense Added", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/service/v2/delete-expense/{expenseId}")
    public ResponseEntity<?> deleteExpense(@CurrentSecurityContext (expression = "authentication?.name") String email,
                                           @PathVariable String expenseId){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found"));

        String userId = user.getUserId();

        if(userId != null || userId != ""){
            expenseTrackerService.deleteExpense(expenseId, userId);
            return new ResponseEntity<>("Expense Deleted", HttpStatus.OK);
        }

        return new ResponseEntity<>("Something went Wrong", HttpStatus.BAD_REQUEST);
    }



}
