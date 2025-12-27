package main.java.com.project5.rcrsms;

import com.project5.rcrsms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    
}