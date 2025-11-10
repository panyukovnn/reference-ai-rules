package ru.panyukovnn.httpintegration.exception;

/**
 * Исключение для ошибок интеграции с внешними сервисами
 */
public class IntegrationException extends BusinessException {

    public IntegrationException(String message) {
        super(message);
    }

    public IntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
