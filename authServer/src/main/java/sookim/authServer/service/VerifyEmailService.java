package sookim.authServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerifyEmailService {
    @Autowired
    private JavaMailSender mailSender;
    private final RedisService redisService;
    private final UserRepository userRepository;

    public void sendMail(String to, String sub, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sub);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendVerifyMail(User user) {
        String verifyLink = "http://localhost:8080/signup/verify/";
        UUID uuid = UUID.randomUUID();
        redisService.setData(uuid.toString(), user.getUsername(),  1000 * 60 * 5);
        sendMail(user.getEmail(),"[WEBSITE] 회원가입 인증메일입니다. 링크를 눌러 인증을 완료해주세요.",verifyLink + uuid.toString());
    }

    public void verifyUserEmail(String key) throws RuntimeException {
        String username = redisService.getData(key);
        User user = userRepository.findByUsername(username);
        if(user==null) throw new RuntimeException("존재하지 않는 유저입니다.");
        user.setRole("ROLE_USER");
        userRepository.save(user);
        redisService.deleteData(key);
    }
}
