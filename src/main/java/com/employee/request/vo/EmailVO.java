package com.employee.request.vo;

import java.util.Map;


public class EmailVO {
	
	private String from;
	private String to;

	private String subject;

	private String text;

	private String messsage;

	private Boolean status;

	private Map<String, Object> messageBody;

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

    public Map<String, Object> getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(Map<String, Object> messageBody) {
		this.messageBody = messageBody;
	}

	public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

	@Override
	public String toString() {
		return "Email [from=" + from + ", to=" + to + ", subject=" + subject + ", text=" + text + ", messsage="
				+ messsage + ", status=" + status + ", messageBody=" + messageBody + ", templateName=" + templateName
				+ "]";
	}
}
