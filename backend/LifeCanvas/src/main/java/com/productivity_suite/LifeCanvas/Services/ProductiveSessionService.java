package com.productivity_suite.LifeCanvas.Services;

import com.productivity_suite.LifeCanvas.Entity.ProductiveSession;
import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.ProductiveSessionRepository;
import com.productivity_suite.LifeCanvas.Responses.ProductiveSessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductiveSessionService {

    @Autowired
    private ProductiveSessionRepository productiveSessionRepository;

    // Implementation
    public ProductiveSessionResponse getSessionFromId(String sessionId){
        ProductiveSession session = productiveSessionRepository.findBySessionId(sessionId)
                .orElseThrow(()-> new RuntimeException("Session Not Found"));

        return convertToProductiveSessionResponse(session);
    }

    public ProductiveSessionResponse createNewSession(UserEntity user) {
        ProductiveSession session = ProductiveSession.builder()
                .sessionId(UUID.randomUUID().toString())
                .user(user)
                .startTime(LocalDateTime.now())
                .build();

        ProductiveSession result = productiveSessionRepository.save(session);

        return convertToProductiveSessionResponse(session);

    }

    public ProductiveSessionResponse stopCurrentSession(String sessionId){
        Optional<ProductiveSession> optional = Optional.ofNullable(productiveSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Session Not Found")));
        ProductiveSession session = optional.get();
        UserEntity user = session.getUser();

        if(user == null){
            throw new RuntimeException("User not Authenticated");
        }

        LocalDateTime endTime = LocalDateTime.now();
        session.setEndTime(endTime);

        long durationSeconds = ChronoUnit.SECONDS.between(session.getStartTime(), endTime);
        session.setDurationTime(durationSeconds);


        session.setSessionDate(LocalDate.now());
        session.setYear(LocalDate.now().getYear());

        LocalDate date = LocalDate.now();
        int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
        session.setWeekNumber(weekNumber);

        ProductiveSession finalSession = productiveSessionRepository.save(session);

        return ProductiveSessionResponse.builder()
                .sessionId(sessionId)
                .user(user)
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .durationTime(session.getDurationTime())
                .build();
    }

    // Converters
    private ProductiveSessionResponse convertToProductiveSessionResponse(ProductiveSession session){
        return ProductiveSessionResponse.builder()
                .sessionId(session.getSessionId())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .user(session.getUser())
                .durationTime(session.getDurationTime())
                .build();
    }

    private ProductiveSession convertToProdutiveSession(ProductiveSessionResponse sessionResponse){
        LocalDate date = LocalDate.now();
        int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());

        return ProductiveSession.builder()
                .sessionId(sessionResponse.getSessionId())
                .user(sessionResponse.getUser())
                .startTime(sessionResponse.getStartTime())
                .endTime(sessionResponse.getEndTime())
                .durationTime(sessionResponse.getDurationTime())
                .sessionDate(LocalDate.now())
                .weekNumber(weekNumber)
                .year(LocalDate.now().getYear())
                .build();
    }
}
