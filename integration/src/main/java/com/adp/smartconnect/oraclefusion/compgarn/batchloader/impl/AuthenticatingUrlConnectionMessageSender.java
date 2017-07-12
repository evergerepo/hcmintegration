package com.adp.smartconnect.oraclefusion.compgarn.batchloader.impl;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

import sun.misc.BASE64Encoder;

public class AuthenticatingUrlConnectionMessageSender extends HttpsUrlConnectionMessageSender {

	private String username;
	private String password;
	private String authorization;

	private String credential() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(getUsername());
		sb.append(':');
		sb.append(getPassword());
		BASE64Encoder enc = new sun.misc.BASE64Encoder();
		String encodedAuthorization = enc.encode(sb.toString().getBytes());
		return encodedAuthorization;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Basic ");
		sb.append(credential());
		authorization = sb.toString();
		super.afterPropertiesSet();
	}

	@Override
	protected void prepareConnection(HttpURLConnection connection) throws IOException {
		super.prepareConnection(connection);
		connection.setRequestProperty("Authorization", authorization);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

}
