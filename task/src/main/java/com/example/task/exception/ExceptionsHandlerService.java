package com.example.task.exception;

import org.springframework.stereotype.Component;

@Component

public class ExceptionsHandlerService {
	private ExceptionsHandlerService() {}

	
	public static BusinessException handleException(Exception e, String methodError) {
		if(e instanceof BusinessException)
			return (BusinessException) e;
		e.printStackTrace();
		return new BusinessException(methodError);
	}
}
