package com.comeon.assignment.playerservice.dao;


import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.comeon.assignment.playerservice.bean.Player;
import com.comeon.assignment.playerservice.repository.PlayerRepository;

@Component
public class PlayerDaoService {
	
	@Autowired
    private PlayerRepository playerRepository;
    
	public Player registerPlayer(Player player) {
		// TODO Auto-generated method stub
		return playerRepository.save(player);
	}
	
	public List<Player> getAllPlayers(){
		// TODO Auto-generated method stub
		return playerRepository.findAll();
	}
		
	public Player getPlayerByEmailId(String email){
		// TODO Auto-generated method stub
		return playerRepository.findByEmail(email);
	}
	
	
    public boolean isValidEmail(String email) {
    	
    	String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (email == null) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }


	
}
