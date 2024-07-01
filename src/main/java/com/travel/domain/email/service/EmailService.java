package com.travel.domain.email.service;

import com.travel.domain.reservations.entity.Reservation;
import com.travel.global.exception.EmailException;
import com.travel.global.exception.type.ErrorType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendReservationConfirmation(String to, Reservation reservation) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("예약 확인서");

            String content = loadTemplate("templates/reservation-confirmation.html", Map.of(
                "name", reservation.getMember().getName(),
                "accommodationName", reservation.getAccommodation().getName(),
                "productName", reservation.getProduct().getName(),
                "checkInDate", reservation.getCheckInDate().toString(),
                "checkOutDate", reservation.getCheckOutDate().toString(),
                "personNumber", String.valueOf(reservation.getPersonNumber()),
                "night", String.valueOf(reservation.getNight()),
                "totalPrice", String.valueOf(reservation.getPrice() * reservation.getNight())
            ));

            helper.setText(content, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailException(ErrorType.EMAIL_SEND_FAILURE);
        }
    }

    private String loadTemplate(String path, Map<String, String> variables) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            String content = new String(Files.readAllBytes(Paths.get(resource.getURI())), "UTF-8");

            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }

            return content;
        } catch (IOException e) {
            throw new EmailException(ErrorType.TEMPLATE_LOAD_FAILURE);
        }
    }
}
