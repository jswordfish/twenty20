package com.twenty20.util;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.twenty20.common.Twenty20Exception;



public class EmailUtil implements Runnable{
	com.twenty20.domain.User		user;
	
	//static String htmlUserRegStr = "";
	
	HtmlEmail	he	= new HtmlEmail();
	
	public EmailUtil(com.twenty20.domain.User user)
		{
			this.user = user;
		}
		
	@Override
	public void run()
		{
			// TODO Auto-generated method stub
			try
				{
					String path = ConfUtil.get("htmlTemplateLocation");
					//Resource resource = new ClassPathResource(path);
					String msg = FileUtils.readFileToString(new File(path));
					String name = user.getFirstName() + " " + user.getLastName();
					msg = msg.replace("$token", user.getToken());
					String ln = ConfUtil.get("line_image");
					String fb = ConfUtil.get("facebook_image");
					String tw = ConfUtil.get("twitter_image");
					msg = msg.replace("$facebook_image", fb);
					msg = msg.replace("$twitter_image", tw);
					msg = msg.replace("$line_image", ln);
					msg = msg.replace("$fullname", user.getFirstName()+" "+user.getLastName());
			
					he.setHtmlMsg(msg);
					String host = ConfUtil.get("hostName");
					String from = ConfUtil.get("sendFrom");
					String fromName = ConfUtil.get("sendFromName");
					String pass = ConfUtil.get("sendFromPwd");
					String smtpPort = ConfUtil.get("smtpPort");
					he.setHostName(host);
					he.addTo(user.getEmail());
					he.setFrom(from, fromName);
					he.setSubject("Verification for User Registration Email from Twenty20");
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
