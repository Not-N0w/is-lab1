package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
    private static final Set<String> TYPES = Set.of("lab-work", "person", "discipline", "coordinates", "location");

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendChangeBroadcast(String type) {
        if (!TYPES.contains(type)) {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
        messagingTemplate.convertAndSend("/topic/" + type, type);
    }
}