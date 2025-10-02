package com.mediator_service.service.scheduler;

import com.mediator_service.service.manager.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionCleanup {

    private final SessionManager sessionManager;

    @Scheduled(fixedDelay = 60000)
    public void cleanup() {
        int removedSessions = 0;

        for (String userId : new HashSet<>(sessionManager.getAll().keySet())) {
            if (!sessionManager.isActive(userId)) {
                sessionManager.remove(userId);
                removedSessions++;
            }
        }

        if (removedSessions > 0) {
            log.info("Cleanup removed {} inactive user sessions", removedSessions);
        }
    }
}
