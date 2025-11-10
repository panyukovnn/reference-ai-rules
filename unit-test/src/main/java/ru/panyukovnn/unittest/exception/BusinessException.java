package ru.panyukovnn.unittest.exception;

public class BusinessException extends RuntimeException {
    private final String code;
    private final String category;

    public BusinessException(String code, String category, String message) {
        super(message);
        this.code = code;
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public String getCategory() {
        return category;
    }
}
