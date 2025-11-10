package ru.panyukovnn.httpintegration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.panyukovnn.httpintegration.client.IdentityClient;
import ru.panyukovnn.httpintegration.dto.AdminResponse;
import ru.panyukovnn.httpintegration.dto.IdentityUserResponse;
import ru.panyukovnn.httpintegration.entity.User;
import ru.panyukovnn.httpintegration.repository.UserRepository;

import java.util.List;

/**
 * Сервис для управления пользователями администраторов
 * Интегрирует IdentityClient с сохранением в БД
 */
@Slf4j
@Service
public class UserService {

    private final IdentityClient identityClient;
    private final UserRepository userRepository;

    public UserService(IdentityClient identityClient, UserRepository userRepository) {
        this.identityClient = identityClient;
        this.userRepository = userRepository;
    }

    /**
     * Получить список администраторов из Identity API и сохранить их в БД
     *
     * @param name    часть имени для поиска
     * @param limit   количество результатов
     * @param offset  смещение для пагинации
     * @param orderBy поле для сортировки
     * @param sortBy  направление сортировки
     * @return список администраторов
     */
    public List<AdminResponse> findAdmins(String name, Integer limit, Integer offset,
                                          String orderBy, String sortBy) {
        log.info("Searching for admins with name: {}", name);

        // Получить пользователей из Identity API
        List<IdentityUserResponse> identityUsers = identityClient.findAdmins(
                name, limit, offset, orderBy, sortBy
        );

        log.info("Found {} users from Identity API", identityUsers.size());

        // Сохранить новых пользователей в БД
        identityUsers.forEach(this::saveUserIfNotExists);

        // Вернуть список администраторов
        return identityUsers.stream()
                .map(this::convertToAdminResponse)
                .toList();
    }

    /**
     * Сохранить пользователя в БД если он не существует
     */
    private void saveUserIfNotExists(IdentityUserResponse identityUser) {
        userRepository.findByExternalId(identityUser.getId())
                .ifPresentOrElse(
                        existing -> log.debug("User {} already exists in DB", identityUser.getId()),
                        () -> {
                            User newUser = User.builder()
                                    .externalId(identityUser.getId())
                                    .name(identityUser.getName())
                                    .email(identityUser.getEmail())
                                    .isAdmin(identityUser.getAdmin() != null && identityUser.getAdmin())
                                    .build();

                            userRepository.save(newUser);
                            log.info("Saved new user: {}", identityUser.getId());
                        }
                );
    }

    /**
     * Конвертировать IdentityUserResponse в AdminResponse
     */
    private AdminResponse convertToAdminResponse(IdentityUserResponse identityUser) {
        User savedUser = userRepository.findByExternalId(identityUser.getId())
                .orElse(null);

        return AdminResponse.builder()
                .id(savedUser != null ? savedUser.getId() : null)
                .name(identityUser.getName())
                .email(identityUser.getEmail())
                .isAdmin(identityUser.getAdmin())
                .build();
    }
}
