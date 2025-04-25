package com.bali.baliairfms.dto.requestdto;

import com.bali.baliairfms.model.enums.NotificationType;

public record NotificationRequest(
        Long passengerId,
        String subject,
        String message,
        NotificationType type
) {
}
