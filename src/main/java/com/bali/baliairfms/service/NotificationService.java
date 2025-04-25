package com.bali.baliairfms.service;

import com.bali.baliairfms.dto.requestdto.NotificationRequest;

public interface NotificationService {

    void send(NotificationRequest request);
}
