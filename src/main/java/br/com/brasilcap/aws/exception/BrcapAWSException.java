package br.com.brasilcap.aws.exception;

public class BrcapAWSException extends Exception{

	private static final long serialVersionUID = -4519981008063755773L;

	public BrcapAWSException() { super(); }
	public BrcapAWSException(String message) { super(message); }
	public BrcapAWSException(String message, Throwable cause) { super(message, cause); }
	public BrcapAWSException(Throwable cause) { super(cause); }	
}
