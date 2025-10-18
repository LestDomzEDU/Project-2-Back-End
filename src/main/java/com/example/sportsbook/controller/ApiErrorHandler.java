package com.example.sportsbook.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiErrorHandler {

  private static Map<String, Object> body(String message) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("message", message);
    return m;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
    // 400 with {"message": "..."}
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body(ex.getMessage()));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex) {
    // Preserve the explicit status & reason passed by controllers
    HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
    if (status == null) status = HttpStatus.BAD_REQUEST;
    String message = ex.getReason() != null ? ex.getReason() : ex.getMessage();
    return ResponseEntity.status(status).body(body(message));
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<Map<String, Object>> handleEmptyResult(EmptyResultDataAccessException ex) {
    // Common case: not found lookups
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body("not found"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
    // Fallback: 500 with a generic message (don’t leak internals)
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body("internal error"));
  }
}
