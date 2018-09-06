package com.twenty20.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import com.twenty20.common.Twenty20Exception;

public class EmailGeneralUtil implements Runnable{
	
	
	HtmlEmail	he	= new HtmlEmail();
private String emailSentTo[];
	
	
	private String subject;
	
	private String message;
	
	String emailSentCC[];
	
	String emailSentBcc[];
	
	
	
	
	public EmailGeneralUtil(String emailSento[], String emailSentCC[], String emailSentBcc[], String subject, String message ) {
		this.emailSentTo = emailSento;
		this.emailSentCC = emailSentCC;
		this.emailSentBcc = emailSentBcc;
		this.subject = subject;
		this.message = message;
	}
	
		
	@Override
	public void run()
		{
			// TODO Auto-generated method stub
			try
				{
			
			
					he.setHtmlMsg(this.message);
					String host = ConfUtil.get("hostName");
					String from = ConfUtil.get("sendFrom");
					String fromName = ConfUtil.get("sendFromName");
					String pass = ConfUtil.get("sendFromPwd");
					String smtpPort = ConfUtil.get("smtpPort");
					he.setHostName(host);
						for(String to : this.emailSentTo) {
							he.addTo(to);
						}
					
						if(this.emailSentCC != null) {
							for(String cc : this.emailSentCC) {
								he.addCc(cc);
							}
						}
						
						if(this.emailSentBcc != null) {
							for(String bcc : this.emailSentBcc) {
								he.addBcc(bcc);
							}
						}
						
						
					he.setFrom(from, fromName);
					he.setSubject(this.subject);
					he.setAuthenticator(new DefaultAuthenticator(from, pass));
					he.setTLS(true);
					System.out.println("smtp port configured  is "+smtpPort);
					he.setSmtpPort(Integer.parseInt(smtpPort));
					he.setSSL(true);
					
					// send the he
					he.send();
					System.out.println("Email Sent");
				}
			catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new Twenty20Exception("Can not send Email", e);
				}
		}

}
