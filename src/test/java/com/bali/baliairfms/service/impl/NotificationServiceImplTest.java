package com.bali.baliairfms.service.impl;

import com.bali.baliairfms.dto.requestdto.NotificationRequest;
import com.bali.baliairfms.exception.ResourceNotFoundException;
import com.bali.baliairfms.model.Passenger;
import com.bali.baliairfms.model.enums.NotificationType;
import com.bali.baliairfms.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Passenger passenger;
    private NotificationRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("john.doe@baliair.com");

        request = new NotificationRequest(
                1L,
                "Flight Update",
                "Your flight has been updated.",
                NotificationType.EMAIL
        );
    }

    @Test
    void testSendNotification_success() {
        // Arrange
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        // Act & Assert
        assertDoesNotThrow(() -> notificationService.send(request));
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void testSendNotification_passengerNotFound() {
        // Arrange
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> notificationService.send(request));
        verify(passengerRepository, times(1)).findById(1L);
    }

}