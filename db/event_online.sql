
CREATE  
    EVENT `check_users_offline`  
    ON SCHEDULE EVERY 1 MINUTE 
    DO  
    
    UPDATE users SET online = 0 WHERE TIME_TO_SEC(TIMEDIFF(NOW(), last_active))/3600 > 0.01
    
