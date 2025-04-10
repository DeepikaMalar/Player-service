package com.comeon.assignment.playerservice.bean;

import java.time.Instant;

public class PlayerTimeLimitSession {
	
	private long totalSecondsSpentToday;
    private Instant sessionStart;
    private int dailyTimeLimitsInSeconds;
    
    public PlayerTimeLimitSession() {
    	
    }
    
	public PlayerTimeLimitSession(long totalSecondsSpentToday, Instant sessionStart, int dailyTimeLimitsInSeconds) {
		super();
		this.totalSecondsSpentToday = totalSecondsSpentToday;
		this.sessionStart = sessionStart;
		this.dailyTimeLimitsInSeconds = dailyTimeLimitsInSeconds;
	}

	public long getTotalSecondsSpentToday() {
		return totalSecondsSpentToday;
	}

	public void setTotalSecondsSpentToday(long totalSecondsSpentToday) {
		this.totalSecondsSpentToday = totalSecondsSpentToday;
	}

	public Instant getSessionStart() {
		return sessionStart;
	}

	public void setSessionStart(Instant sessionStart) {
		this.sessionStart = sessionStart;
	}

	public int getDailyTimeLimitsInSeconds() {
		return dailyTimeLimitsInSeconds;
	}

	public void setDailyTimeLimitsInSeconds(int dailyTimeLimitsInSeconds) {
		this.dailyTimeLimitsInSeconds = dailyTimeLimitsInSeconds;
	}
    
}
