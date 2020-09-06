package com.example.jwt_demo.edge_common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jwt_demo.edge_common.dtos.ProxySetting;

/**
 * HttpRequest このクラスはスペースを%20に変換する
 */
public class HttpRequestUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

	private static final int TIMEOUT = 30; // 30 sec
	private static final int CONNECTION_TIMEOUT = 7200; // 2hour
	private static final int RETRY_COUNT = 3;
	private static final int RETRY_WAIT_MILLSEC = 1000 * 60; // 1 min

	private HttpRequestUtil() {
	}

	public static HttpRequestUtil getInstace() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final HttpRequestUtil INSTANCE = new HttpRequestUtil();
	}

	/**
	 * 指定秒待機後に指定回数リトライする
	 *
	 */
	private static class CustomRetryHandler implements HttpRequestRetryHandler {
		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			if (executionCount > RETRY_COUNT) {
				return false;
			}
			try {
				Thread.sleep(RETRY_WAIT_MILLSEC);
			} catch (InterruptedException e) {
			}
			return true;
		}
	}

	private RequestConfig createConfig(ProxySetting proxySetting) {
		RequestConfig config;
		if (proxySetting != null && StringUtils.isNotEmpty(proxySetting.getHost())) {
			HttpHost proxy = new HttpHost(proxySetting.getHost(), proxySetting.getPort());
			config = RequestConfig.custom()
					.setConnectTimeout(CONNECTION_TIMEOUT * 1000)
					.setConnectionRequestTimeout(TIMEOUT * 1000)
					.setSocketTimeout(CONNECTION_TIMEOUT * 1000)
					.setProxy(proxy)
					.build();
		} else {
			config = RequestConfig.custom()
					.setConnectTimeout(CONNECTION_TIMEOUT * 1000)
					.setConnectionRequestTimeout(TIMEOUT * 1000)
					.setSocketTimeout(CONNECTION_TIMEOUT * 1000)
					.build();
		}
		return config;
	}

	/**
	 * GETメソッド
	 *
	 * @param accessToken
	 * @param url
	 * @return
	 */
	public String get(String accessToken, String url, ProxySetting proxySetting) {
		HttpGet httpGet = new HttpGet(url.replaceAll(" ", "%20"));
		httpGet.addHeader("Authorization", accessToken);
		logger.info("url {}", url);
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(createConfig(proxySetting))
				.setRetryHandler(new CustomRetryHandler()).build()) {
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				}
			}
		} catch (IOException e) {
			logger.error("error. {}", e.toString());
		}
		return null;
	}

	/**
	 * POSTメソッド 登録
	 * JSON
	 *
	 */
	public Integer post(String accessToken, String url, String json, ProxySetting proxySetting) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(createConfig(proxySetting))
				.setRetryHandler(new CustomRetryHandler()).build()) {
			HttpPost httpPost = new HttpPost(url.replaceAll(" ", "%20"));
			httpPost.addHeader("Authorization", accessToken);
			httpPost.addHeader("Content-type", "application/json");
			httpPost.setEntity(new StringEntity(json, "utf-8"));
			logger.info("url {}", url);
			logger.info("proxySetting {}", proxySetting);
			try (CloseableHttpResponse responseBody = httpClient.execute(httpPost)) {
				return responseBody.getStatusLine().getStatusCode();
			}
		} catch (IOException e) {
			logger.error("error. {}", e.toString());
		}
		return HttpStatus.SC_BAD_REQUEST;
	}


	/**
	 * PUTメソッド 更新
	 *
	 * @param accessToken
	 * @param url
	 * @param json
	 * @return
	 */
	public Integer put(String accessToken, String url, ProxySetting proxySetting) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(createConfig(proxySetting))
				.setRetryHandler(new CustomRetryHandler()).build()) {
			HttpPut httpPut = new HttpPut(url.replaceAll(" ", "%20"));
			httpPut.addHeader("Authorization", accessToken);
			logger.info("url {}", url);
			logger.info("proxySetting {}", proxySetting);
			try (CloseableHttpResponse responseBody = httpClient.execute(httpPut)) {
				return responseBody.getStatusLine().getStatusCode();
			}
		} catch (IOException e) {
			logger.error("error. {}", e.toString());
		}
		return HttpStatus.SC_BAD_REQUEST;
	}

	/**
	 * PUTメソッドで更新(json付き)
	 *
	 * @param accessToken
	 * @param url
	 * @param json
	 * @return
	 */
	public Integer put(String accessToken, String url, String json, ProxySetting proxySetting) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(createConfig(proxySetting))
				.setRetryHandler(new CustomRetryHandler()).build()) {
			HttpPut httpPut = new HttpPut(url.replaceAll(" ", "%20"));
			httpPut.addHeader("Authorization", accessToken);
			httpPut.addHeader("Content-type", "application/json");
			httpPut.setEntity(new StringEntity(json, "utf-8"));
			logger.debug("updateJson {}", json);
			logger.info("url {}", url);
			logger.info("proxySetting {}", proxySetting);
			try (CloseableHttpResponse responseBody = httpClient.execute(httpPut)) {
				return responseBody.getStatusLine().getStatusCode();
			}
		} catch (IOException e) {
			logger.error("error. {}", e.getMessage());
		}
		return HttpStatus.SC_BAD_REQUEST;
	}

	/**
	 * POSTメソッド 登録
	 *
	 */
	public Integer postWithoutAuthentication(String url, String json, ProxySetting proxySetting) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(createConfig(proxySetting))
				.setRetryHandler(new CustomRetryHandler()).build()) {
			HttpPost httpPost = new HttpPost(url.replaceAll(" ", "%20"));
			httpPost.addHeader("Content-type", "application/json");
			httpPost.setEntity(new StringEntity(json, "utf-8"));
			logger.info("url {}", url);
			logger.info("proxySetting {}", proxySetting);
			try (CloseableHttpResponse responseBody = httpClient.execute(httpPost)) {
				return responseBody.getStatusLine().getStatusCode();
			}
		} catch (IOException e) {
			logger.error("error. {}", e.toString());
		}
		return HttpStatus.SC_BAD_REQUEST;
	}
}
