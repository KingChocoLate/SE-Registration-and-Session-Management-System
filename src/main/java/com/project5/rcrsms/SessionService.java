package com.project5.rcrsms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionService {

    @Autowired
    private final SessionRepository sessionRepository;
    @Autowired
    private final ConferenceRepository conferenceRepository;
    @Autowired
    private final UserRepository userRepository;

    public SessionService(SessionRepository sessionRepository,
            ConferenceRepository conferenceRepository,
            UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.conferenceRepository = conferenceRepository;
        this.userRepository = userRepository;
    }


    public Session createSession(Session session) {
        validateSession(session);
        return sessionRepository.save(session);
    }

  
    public Session createSessionForConference(Long conferenceId, Session session) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));

        session.setConference(conference);
        validateSession(session);
        return sessionRepository.save(session);
    }

  
    @Transactional(readOnly = true)
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

   
    @Transactional(readOnly = true)
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

   
    @Transactional(readOnly = true)
    public List<Session> getSessionsByConference(Conference conference) {
        return sessionRepository.findByConference(conference);
    }

  
    @Transactional(readOnly = true)
    public List<Session> getSessionsByConferenceId(Long conferenceId) {
        return sessionRepository.findByConferenceConferenceId(conferenceId);
    }

  
    @Transactional(readOnly = true)
    public List<Session> getSessionsByChair(User chair) {
        return sessionRepository.findByChair(chair);
    }

    // Read - Get by chair ID
    @Transactional(readOnly = true)
    public List<Session> getSessionsByChairId(Long chairId) {
        return sessionRepository.findByChairUserId(chairId);
    }

    // Read - Search by title
    @Transactional(readOnly = true)
    public List<Session> searchSessionsByTitle(String keyword) {
        return sessionRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Read - Get sessions by date range
    @Transactional(readOnly = true)
    public List<Session> getSessionsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return sessionRepository.findBySessionTimeBetween(startTime, endTime);
    }

    // Read - Get upcoming sessions
    @Transactional(readOnly = true)
    public List<Session> getUpcomingSessions() {
        LocalDateTime now = LocalDateTime.now();
        return sessionRepository.findBySessionTimeGreaterThanEqualOrderBySessionTimeAsc(now);
    }

    // Read - Get upcoming sessions for a conference
    @Transactional(readOnly = true)
    public List<Session> getUpcomingSessionsForConference(Long conferenceId) {
        LocalDateTime now = LocalDateTime.now();
        return sessionRepository.findByConferenceConferenceIdAndSessionTimeGreaterThanEqual(conferenceId, now);
    }

    // Update
    public Session updateSession(Long id, Session sessionDetails) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));

        session.setTitle(sessionDetails.getTitle());
        session.setSessionTime(sessionDetails.getSessionTime());
        session.setConference(sessionDetails.getConference());
        session.setChair(sessionDetails.getChair());

        validateSession(session);
        return sessionRepository.save(session);
    }

    // Assign chair to session
    public Session assignChair(Long sessionId, Long chairId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));

        User chair = userRepository.findById(chairId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + chairId));

        session.setChair(chair);
        return sessionRepository.save(session);
    }

    // Remove chair from session
    public Session removeChair(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));

        session.setChair(null);
        return sessionRepository.save(session);
    }

    // Delete
    public void deleteSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));
        sessionRepository.delete(session);
    }

    // Delete all sessions for a conference
    public void deleteSessionsByConference(Long conferenceId) {
        List<Session> sessions = sessionRepository.findByConferenceConferenceId(conferenceId);
        sessionRepository.deleteAll(sessions);
    }

    // Check if session exists
    @Transactional(readOnly = true)
    public boolean sessionExists(Long id) {
        return sessionRepository.existsById(id);
    }

    // Count sessions
    @Transactional(readOnly = true)
    public long countSessions() {
        return sessionRepository.count();
    }

    // Count sessions by conference
    @Transactional(readOnly = true)
    public long countSessionsByConference(Long conferenceId) {
        return sessionRepository.findByConferenceConferenceId(conferenceId).size();
    }

    // Count sessions by chair
    @Transactional(readOnly = true)
    public long countSessionsByChair(Long chairId) {
        return sessionRepository.findByChairUserId(chairId).size();
    }

    // Validation helper
    private void validateSession(Session session) {
        if (session.getConference() == null) {
            throw new IllegalArgumentException("Session must be associated with a conference");
        }

        if (session.getSessionTime() != null && session.getConference() != null) {
            LocalDateTime sessionDateTime = session.getSessionTime();
            LocalDateTime conferenceStart = session.getConference().getStartDate().atStartOfDay();
            LocalDateTime conferenceEnd = session.getConference().getEndDate().atTime(23, 59, 59);

            if (sessionDateTime.isBefore(conferenceStart) || sessionDateTime.isAfter(conferenceEnd)) {
                throw new IllegalArgumentException("Session time must be within conference dates");
            }
        }
    }
}