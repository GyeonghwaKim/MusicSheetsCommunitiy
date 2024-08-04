package com.example.youtubeSheet;

import com.example.youtubeSheet.exception.DeserializationException;
import com.example.youtubeSheet.exception.MultipartFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(MultipartFileException.class)
    public ResponseEntity<?> handleMultipartFileException(MultipartFileException ex){
        String message = ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DeserializationException.class)
    public ResponseEntity<?> handleDeserializationException(DeserializationException ex){
        String message = ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
