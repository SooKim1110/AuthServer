package sookim.authServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUserList(){
        return userRepository.findAll();
    }

    public void saveUser(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setRole("ROLE_VERIFY_REQUIRED");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}

