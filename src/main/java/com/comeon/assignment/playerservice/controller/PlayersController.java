package com.comeon.assignment.playerservice.controller;

import java.time.Duration;
import java.time.Instant;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.comeon.assignment.playerservice.bean.Login;
import com.comeon.assignment.playerservice.bean.Player;
import com.comeon.assignment.playerservice.bean.PlayerTimeLimitSession;
import com.comeon.assignment.playerservice.dao.PlayerDaoService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/player")
public class PlayersController {
	
	@Autowired
	private PlayerDaoService playerDaoService;
	
	
	public static final Map<Long, PlayerTimeLimitSession> sessionDataMap = new ConcurrentHashMap<>();

	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Player player) {
		
		try {
			Player existingPlayer = playerDaoService.getPlayerByEmailId(player.getEmail());		
			if(existingPlayer!=null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player exists");
			}
			boolean validEmail = playerDaoService.isValidEmail(player.getEmail());
			if(!validEmail) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email");
			}
			playerDaoService.registerPlayer(player);		
			return ResponseEntity.created(null).build();
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

		
	}
	
	 @PostMapping("/login")
     public ResponseEntity<String> login(@RequestBody Login login, HttpSession session) {
		 
		try { 
			
			boolean validEmail = playerDaoService.isValidEmail(login.getEmail());
			if(!validEmail) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email");
			}
			Player player = playerDaoService.getPlayerByEmailId(login.getEmail());
				
			if(player==null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player  not found");			
			}
			if(!player.getPassword().equals(login.getPassword())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credential");		
			}
			
			PlayerTimeLimitSession playerTimeLimitSession = sessionDataMap.getOrDefault(player.getId(), new PlayerTimeLimitSession());
			long totalSpentToday = playerTimeLimitSession.getTotalSecondsSpentToday();
			int dailyLimit = playerTimeLimitSession.getDailyTimeLimitsInSeconds();	
			if (playerTimeLimitSession.getDailyTimeLimitsInSeconds()>0 && totalSpentToday >= dailyLimit) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Daily time limit reached");
		    }
				
			playerTimeLimitSession.setSessionStart(Instant.now());
			sessionDataMap.put(player.getId(), playerTimeLimitSession);
			session.setAttribute("playerId", player.getId());     
		    return ResponseEntity.status(HttpStatus.OK).body("Login successful");
		    
		} catch(Exception e){
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
     }


	@PostMapping("/logout")
     public ResponseEntity<String> logout(HttpSession session) {
    	
		Long playerId = (Long) session.getAttribute("playerId");  
    	 
    	if (playerId == null) {
    	return ResponseEntity.badRequest().body("Not logged in");
        }
   	    PlayerTimeLimitSession playerTimeLimitSession = sessionDataMap.get(playerId);	  
   	    if (playerTimeLimitSession != null && playerTimeLimitSession.getSessionStart() != null) {
   	        long totalSecondsSpentInCurrentSession = Duration.between(playerTimeLimitSession.getSessionStart(), Instant.now()).getSeconds();
   	        playerTimeLimitSession.setTotalSecondsSpentToday(totalSecondsSpentInCurrentSession);	        
    	}
        session.invalidate();
   	    return ResponseEntity.ok("Logged out successfully");
    	
     }
    
    @PostMapping("/set-limit")
    public ResponseEntity<String> setLimit(@RequestBody Integer limitInSeconds, HttpSession session) {
    	
    	Long playerId = (Long) session.getAttribute("playerId");
        if (session == null || playerId == null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player is not active"); 
        }
        PlayerTimeLimitSession playerTimeLimitSession = sessionDataMap.get(playerId);
        if(playerTimeLimitSession!=null) {
        	playerTimeLimitSession.setDailyTimeLimitsInSeconds(limitInSeconds);
            return ResponseEntity.ok("Time Limit updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
    
    }
    

}
	