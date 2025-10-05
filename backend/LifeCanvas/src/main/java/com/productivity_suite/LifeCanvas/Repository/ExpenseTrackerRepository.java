package com.productivity_suite.LifeCanvas.Repository;

import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseTrackerRepository extends JpaRepository<ExpenseTrackerEntity, Long> {
    List<ExpenseTrackerEntity> findByUser_UserId(String userId);

    List<ExpenseTrackerEntity> findByUser_UserIdAndTransactionDateBetween(String userId, LocalDate start, LocalDate end);

}
