package com.productivity_suite.LifeCanvas.Repository;


import com.productivity_suite.LifeCanvas.Entity.ProductiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductiveSessionRepository extends JpaRepository<ProductiveSession, Long>{

    Optional<ProductiveSession> findBySessionId(String sessionId);

}
