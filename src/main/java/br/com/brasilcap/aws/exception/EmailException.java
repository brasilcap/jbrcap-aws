package br.com.brasilcap.aws.exception;

public final class EmailException extends Exception{

	private static final long serialVersionUID = -4519981008063755773L;

	public EmailException() { super(); }
	public EmailException(String message) { super(message); }
	public EmailException(String message, Throwable cause) { super(message, cause); }
	public EmailException(Throwable cause) { super(cause); }
	
}
