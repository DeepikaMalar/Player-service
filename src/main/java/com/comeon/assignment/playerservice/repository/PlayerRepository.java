package com.comeon.assignment.playerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.comeon.assignment.playerservice.bean.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	Player findByEmail(String email);

	

}
