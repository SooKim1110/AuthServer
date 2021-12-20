package sookim.authServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sookim.authServer.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
