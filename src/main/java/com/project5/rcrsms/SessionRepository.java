package main.java.com.project5.rcrsms;

import main.java.com.project5.rcrsms.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRepository extends JpaRepository<Session, Long> {
    
}
