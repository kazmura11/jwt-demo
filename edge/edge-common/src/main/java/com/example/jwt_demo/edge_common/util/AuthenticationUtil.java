package com.example.jwt_demo.edge_common.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jwt_demo.edge_common.dtos.AuthenticationDto;
import com.example.jwt_demo.edge_common.dtos.ProxySetting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 認証クラス
 *
 */
public class AuthenticationUtil {
	private Logger logger = LoggerFactory.getLogger(AuthenticationUtil.class);

	private static final int TIMEOUT = 30;

	private AuthenticationUtil() {
	}

	public static AuthenticationUtil getInstace() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final AuthenticationUtil INSTANCE = new AuthenticationUtil();
	}

	private RequestConfig createConfig(ProxySetting proxySetting) {
		RequestConfig config;
		if (proxySetting != null && StringUtils.isNotEmpty(proxySetting.getHost())) {
			HttpHost proxy = new HttpHost(proxySetting.getHost(), proxySetting.getPort());
			config = RequestConfig.custom()
					.setConnectTimeout(TIMEOUT * 1000)
					.setConnectionRequestTimeout(TIMEOUT * 1000)
					.setSocketTimeout(TIMEOUT * 1000)
					.setProxy(proxy)
					.build();
		} else {
			config = RequestConfig.custom()
					.setConnectTimeout(TIMEOUT * 1000)
					.setConnectionRequestTimeout(TIMEOUT * 1000)
					.setSocketTimeout(TIMEOUT * 1000)
					.build();
		}
		return config;
	}
	
	public String getServerAccessToken(String emailAddress, String password, String url, ProxySetting proxySetting) {
		logger.info("start.getAccessToken {}", "start");
		String accessToken = null;
		AuthenticationDto authenticationDto = new AuthenticationDto(emailAddress, password);

		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(authenticationDto);
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				HttpPost httpPost = new HttpPost(url);
				httpPost.addHeader("accept", "application/json");
				httpPost.addHeader("Content-type", "application/json");
				httpPost.setEntity(new StringEntity(json));
				logger.info("proxySetting {}", proxySetting);
				if (proxySetting != null) {
					httpPost.setConfig(createConfig(proxySetting));
				}
				try (CloseableHttpResponse responseBody = httpClient.execute(httpPost)) {
					if (responseBody.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						logger.info("success.getAccessToken {}", responseBody.getStatusLine().getStatusCode());
						Header authorizationHeader = responseBody.getFirstHeader("Authorization");
						accessToken = authorizationHeader.getValue();
					} else {
						logger.error("error.getAccessToken {}", responseBody.getStatusLine().getStatusCode());
					}
				}
			} catch (IOException e) {
				logger.error("error. {}", e.getMessage());
			}
		} catch (JsonProcessingException e) {
			logger.error("error. {}", e.getMessage());
		}

		return accessToken;
	}
}
