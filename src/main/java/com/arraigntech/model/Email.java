package com.arraigntech.model;

import java.util.Map;

/**
 * 
 * @author tulabandula.kumar
 *
 */
public class Email {
	
	private String from;
	private String to;

	private String subject;

	private String text;

	private String messsage;

	private Boolean status;

	private Map messageBody;

	private String templateName;

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMesssage() {
		return messsage;
	}

	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Map getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(Map messageBody) {
		this.messageBody = messageBody;
	}

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


}
