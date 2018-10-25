package br.com.brasilcap.aws.dto;

import com.amazonaws.services.sqs.model.Message;

public class RetornoSQSMonitorDTO {
	
	private Message bodyMessage;
	private String receiptHandle;
	private String code;
	private String message;
	private String messageId;
	private String subject;
	private String arn;
	
	public Message getBodyMessage() {
		return bodyMessage;
	}
	
	public void setBodyMessage(Message bodyMessage) {
		this.bodyMessage = bodyMessage;
	}
	
	public String getReceiptHandle() {
		return receiptHandle;
	}
	
	public void setReceiptHandle(String receiptHandle) {
		this.receiptHandle = receiptHandle;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessageId() {
		return messageId;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getArn() {
		return arn;
	}
	
	public void setArn(String arn) {
		this.arn = arn;
	}
}
