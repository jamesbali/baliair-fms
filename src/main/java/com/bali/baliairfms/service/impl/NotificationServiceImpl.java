package com.bali.baliairfms.service.impl;


import com.bali.baliairfms.dto.requestdto.NotificationRequest;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.model.Passenger;
import com.bali.baliairfms.repository.PassengerRepository;
import com.bali.baliairfms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final PassengerRepository passengerRepository;

    @Override
    public void send(NotificationRequest request) {
        Passenger passenger = passengerRepository.findById(request.passengerId())
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        //  email/SMS sending
        System.out.printf(
                "📨 [%s] To: %s | Subject: %s | Message: %s%n",
                request.type(), passenger.getEmail(), request.subject(), request.message()
        );
    }

    
}
