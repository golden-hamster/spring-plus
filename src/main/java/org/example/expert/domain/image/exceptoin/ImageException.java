package org.example.expert.domain.image.exceptoin;

public class ImageException extends RuntimeException{

    private static final String MESSAGE = "이미지 관련 문제가 발생했습니다.";

    public ImageException() {super(MESSAGE);}

    public ImageException(String message) {super(message);}
}
