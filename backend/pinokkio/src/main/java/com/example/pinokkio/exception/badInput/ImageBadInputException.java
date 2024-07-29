package com.example.pinokkio.exception.badInput;

import com.example.pinokkio.exception.base.BadInputException;
import java.util.Map;

public class ImageBadInputException extends BadInputException {
    public ImageBadInputException(String filePath) {
        super("IMAGE_01", "파일 형식이 잘못되었습니다.", Map.of("filePath", filePath));
    }
}