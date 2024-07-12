package com.example.youtubeSheet.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YoutubeValidator.class)
public @interface Youtube {

    String message() default "형식에 맞지 않는 url입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
