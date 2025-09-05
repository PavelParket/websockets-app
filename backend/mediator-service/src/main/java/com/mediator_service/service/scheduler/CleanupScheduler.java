package com.mediator_service.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupScheduler {

    private final RoomCleanup roomCleanup;

    private final SessionCleanup sessionCleanup;

    @Scheduled(fixedDelay = 60000)
    public void cleanupSessions() {
        sessionCleanup.cleanup();
    }

    @Scheduled(fixedDelay = 300000)
    public void cleanupRooms() {
        roomCleanup.cleanup();
    }
}
