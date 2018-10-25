package br.com.brasilcap.aws.dto;

public final class EmailDTO {
			
	private String from;
	private String to;
	private String configSet;
	private String subject;
	private String htmlBody;
	private String textBody;
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getConfigSet() {
		return configSet;
	}
	
	public void setConfigSet(String configSet) {
		this.configSet = configSet;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getHtmlBody() {
		return htmlBody;
	}
	
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}
	
	public String getTextBody() {
		return textBody;
	}
	
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
}
