package com.example.tts.exception;

    import com.example.tts.dto.ErrorResponse;

	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;

	import org.springframework.web.bind.MethodArgumentNotValidException;
	import org.springframework.web.bind.annotation.ExceptionHandler;
	import org.springframework.web.bind.annotation.RestControllerAdvice;

	@RestControllerAdvice
	public class GlobalExceptionHandler {

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorResponse> handleValidation(
	            MethodArgumentNotValidException exception) {

	        String message = exception
	                .getBindingResult()
	                .getFieldError()
	                .getDefaultMessage();

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ErrorResponse(message));
	    }

	    @ExceptionHandler(TtsException.class)
	    public ResponseEntity<ErrorResponse> handleTtsException(
	            TtsException exception) {

	        return ResponseEntity
	                .status(HttpStatus.BAD_GATEWAY)
	                .body(new ErrorResponse(
	                        exception.getMessage()
	                ));
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorResponse> handleGeneralException(
	            Exception exception) {

	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ErrorResponse(
	                        "Something went wrong. Please try again."
	                ));
	    }
	}


