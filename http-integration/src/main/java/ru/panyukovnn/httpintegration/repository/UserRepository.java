package ru.panyukovnn.httpintegration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.panyukovnn.httpintegration.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по внешнему ID из Identity API
     */
    Optional<User> findByExternalId(String externalId);
}
