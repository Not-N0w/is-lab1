package com.github.not.n0w.islab1.aspect;

import com.github.not.n0w.islab1.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class WebSocketAspect {
    private final WebSocketService webSocketService;

    private String wsType(JoinPoint jp) {
        Object target = jp.getTarget();
        if (target instanceof LabWorkService) return "lab-work";
        if (target instanceof CoordinatesService) return "coordinates";
        if (target instanceof DisciplineService) return "discipline";
        if (target instanceof PersonService) return "person";
        if (target instanceof LocationService) return "location";
        return null;
    }

    @AfterReturning(
            "(execution(public * com.github.not.n0w.islab1.service..*.create*(..)) || " +
                    "execution(public * com.github.not.n0w.islab1.service..*.update*(..)) || " +
                    "execution(public * com.github.not.n0w.islab1.service..*.delete*(..)) || " +
                    "execution(public * com.github.not.n0w.islab1.service..*.add*(..))) && " +
                    "!within(com.github.not.n0w.islab1.service.impl.UserServiceImpl)"
    )
    public void wsNotify(JoinPoint joinPoint) throws Throwable {
        String wsType = wsType(joinPoint);
        log.info("Aspect call: ws type: {}", wsType);
        if (wsType != null) webSocketService.sendChangeBroadcast(wsType);
    }
}
