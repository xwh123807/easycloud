package org.myfly.platform.core.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController 异常系统级处理
 * @author xiangwanhong
 *
 */
@ControllerAdvice(annotations=RestController.class)
public class RestControllerExceptionHandlerAdvice {
	@ExceptionHandler
	public ResponseEntity<String> exceptionHandler(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("服务器异常:" + e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
