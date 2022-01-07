package com.example.task.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex
     *            MissingServletRequestParameterException
     * @param headers
     *            HttpHeaders
     * @param status
     *            HttpStatus
     * @param request
     *            WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	String error = ex.getParameterName() + " parameter is missing";
	return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex
     *            HttpMediaTypeNotSupportedException
     * @param headers
     *            HttpHeaders
     * @param status
     *            HttpStatus
     * @param request
     *            WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	StringBuilder builder = new StringBuilder();
	builder.append(ex.getContentType());
	builder.append(" media type is not supported. Supported media types are ");
	ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
	return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex
     *            HttpMessageNotReadableException
     * @param headers
     *            HttpHeaders
     * @param status
     *            HttpStatus
     * @param request
     *            WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	ServletWebRequest servletWebRequest = (ServletWebRequest) request;
	System.out.println("{} to {}" + servletWebRequest.getHttpMethod() + servletWebRequest.getRequest().getServletPath());
	String error = "Malformed JSON request";
	return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex
     *            HttpMessageNotWritableException
     * @param headers
     *            HttpHeaders
     * @param status
     *            HttpStatus
     * @param request
     *            WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	String error = "Error writing JSON output";
	return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	ApiError apiError = new ApiError(BAD_REQUEST);
	apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
	apiError.setDebugMessage(ex.getMessage());
	return buildResponseEntity(apiError);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
	return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex
     *            the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
	if (ex.getCause() instanceof ConstraintViolationException) {
	    return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "error_relatedData", ex.getCause()));
	}
	return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, ex));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleBusinessError(BusinessException ex) {
	ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
	return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
	ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
	return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
	return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
