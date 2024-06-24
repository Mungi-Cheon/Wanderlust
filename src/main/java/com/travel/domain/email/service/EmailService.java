package com.travel.domain.email.service;

import com.travel.domain.reservations.entity.Reservations;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender emailSender;

  public void sendReservationConfirmation(String to, Reservations reservation) {
    MimeMessage message = emailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(to);
      helper.setSubject("예약 확인서");

      String content = "<div style='font-family: Arial, sans-serif; line-height: 1.5;'>"
          + "<h2>안녕하세요 " + reservation.getUser().getUsername() + " 님,</h2>"
          + "<p>고객님의 예약이 성공적으로 완료되었습니다.</p>"
          + "<p><strong>숙소 이름:</strong> " + reservation.getAccommodation().getName() + "</p>"
          + "<p><strong>객실 이름:</strong> " + reservation.getProduct().getName() + "</p>"
          + "<p><strong>체크인 날짜:</strong> " + reservation.getCheckInDate() + "</p>"
          + "<p><strong>체크아웃 날짜:</strong> " + reservation.getCheckOutDate() + "</p>"
          + "<p><strong>숙박 인원:</strong> " + reservation.getPersonNumber() + "명</p>"
          + "<p><strong>숙박 일수:</strong> " + reservation.getNight() + "박</p>"
          + "<p><strong>총 금액:</strong> " + reservation.getPrice() * reservation.getNight() + "원</p>"
          + "<br>"
          + "<p>저희 숙소를 선택해 주셔서 감사합니다.</p>"
          + "<p>즐거운 여행 되세요!</p>"
          + "<br>"
          + "<p>감사합니다,</p>"
          + "<p><strong>" + reservation.getAccommodation().getName() + " 팀</strong></p>"
          + "</div>";

      helper.setText(content, true);

      emailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("이메일 전송에 실패했습니다.", e);
    }
  }
}

