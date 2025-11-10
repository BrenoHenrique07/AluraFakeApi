package br.com.alura.AluraFake.core.exception.type;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
