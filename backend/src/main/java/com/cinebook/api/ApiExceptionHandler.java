package com.cinebook.api;
import java.util.Map;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;
@RestControllerAdvice public class ApiExceptionHandler{
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> bad(IllegalArgumentException e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}
 @ExceptionHandler(Exception.class) ResponseEntity<?> fail(Exception e){return ResponseEntity.status(500).body(Map.of("error","Unexpected server error"));}
}
