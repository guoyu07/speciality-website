package com.hiczp.web.speciality.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by czp on 17-2-24.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Article not found")
public class ArticleNotFoundException extends RuntimeException {
}
