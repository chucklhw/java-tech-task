package com.rezdy.lunch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid recipe")
public class ResourceNotFoundException extends RuntimeException {
}
