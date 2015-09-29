package com.fluxit.camel.component.n4.exception;

import java.text.MessageFormat;

import org.apache.camel.RuntimeCamelException;

public class InvalidParameterException extends RuntimeCamelException {

	private static final long serialVersionUID = 1L;

	public InvalidParameterException() {
	}

	public InvalidParameterException(String parameter, String value) {
		super(
				MessageFormat
						.format("Valor inválido al intentar asignar al parámetro {0} el valor {1}",
								parameter, value));
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParameterException(Throwable cause) {
		super(cause);
	}

}
