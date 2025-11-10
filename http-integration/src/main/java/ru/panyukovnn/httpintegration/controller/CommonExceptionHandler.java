package ru.panyukovnn.httpintegration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.panyukovnn.httpintegration.dto.common.CommonResponse;
import ru.panyukovnn.httpintegration.exception.BusinessException;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public CommonResponse<Void> handleLinkShortenerException(BusinessException e) {
        log.warn(e.getMessage(), e);

        return CommonResponse.<Void>builder()
            .id(UUID.randomUUID())
            .errorMessage("Бизнес исключение: " + e.getMessage())
            .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e) {
        log.error("Непредвиденное исключение: {}", e.getMessage(), e);

        return CommonResponse.builder()
            .id(UUID.randomUUID())
            .errorMessage("Непредвиденное исключение: " + e.getMessage())
            .build();
    }
}
