package com.productivity_suite.LifeCanvas.Repository;

import com.productivity_suite.LifeCanvas.Entity.ExpenseTrackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseTrackerRepository extends JpaRepository<ExpenseTrackerEntity, Object> {
    List<ExpenseTrackerEntity> findByUserId(String userId);

}
