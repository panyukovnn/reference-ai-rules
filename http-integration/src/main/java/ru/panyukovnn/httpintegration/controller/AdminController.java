package ru.panyukovnn.httpintegration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.panyukovnn.httpintegration.dto.AdminResponse;
import ru.panyukovnn.httpintegration.dto.common.CommonResponse;
import ru.panyukovnn.httpintegration.service.UserService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для работы с администраторами
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить список администраторов
     *
     * @param name    часть имени пользователя для поиска
     * @param limit   количество результатов (по умолчанию 10)
     * @param offset  смещение для пагинации (по умолчанию 0)
     * @param orderBy поле для сортировки (по умолчанию "name")
     * @param sortBy  направление сортировки (по умолчанию "ASC")
     * @return список администраторов
     */
    @GetMapping
    public CommonResponse<List<AdminResponse>> getAdmins(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "orderBy", required = false, defaultValue = "name") String orderBy,
            @RequestParam(value = "sortBy", required = false, defaultValue = "ASC") String sortBy
    ) {
        log.info("GET /api/v1/admins - name={}, limit={}, offset={}, orderBy={}, sortBy={}",
                name, limit, offset, orderBy, sortBy);

        List<AdminResponse> admins = userService.findAdmins(name, limit, offset, orderBy, sortBy);

        log.info("Returning {} admins", admins.size());
        return CommonResponse.<List<AdminResponse>>builder()
            .id(UUID.randomUUID())
            .body(admins)
            .build();
    }
}
