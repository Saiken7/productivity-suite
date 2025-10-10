package com.productivity_suite.LifeCanvas.Controller;

import com.productivity_suite.LifeCanvas.Entity.UserEntity;
import com.productivity_suite.LifeCanvas.Repository.UserRepository;
import com.productivity_suite.LifeCanvas.Requests.ProductiveSessionStop;
import com.productivity_suite.LifeCanvas.Responses.ProductiveSessionResponse;
import com.productivity_suite.LifeCanvas.Services.ProductiveSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductiveSessionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductiveSessionService productiveSessionService;

    @GetMapping("/api/service/v2/get-session/{sessionId}")
    public ResponseEntity<?> getSession(@CurrentSecurityContext(expression = "authentication?.name") String email,
                                        @PathVariable String sessionId){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found. Please Login again"));

        String userId = user.getUserId();
        if(userId != null){
            ProductiveSessionResponse response = productiveSessionService.getSessionFromId(sessionId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/service/v2/create-session")
    public ResponseEntity<?> createSession(@CurrentSecurityContext(expression = "authentication?.name") String email){

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        if(user != null){
            ProductiveSessionResponse response = productiveSessionService.createNewSession(user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/service/v2/stop-session")
    public ResponseEntity<?> stopSession(@CurrentSecurityContext(expression = "authentication?.name")String email,
                                         @RequestBody ProductiveSessionStop request){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        if(user!=null){
            ProductiveSessionResponse response = productiveSessionService.stopCurrentSession(request.getSessionId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Something went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
