package sookim.authServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sookim.authServer.domain.User;
import sookim.authServer.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUserList() throws Exception {
        return userRepository.findAll();
    }
}

