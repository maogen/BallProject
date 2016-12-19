package com.zgmao.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 相关地址配置
 * @author mzg
 *
 */
@Component
@ConfigurationProperties(prefix = "web_url")
public class WebUrl {
	private String getBallNumberUrl;

	public String getGetBallNumberUrl() {
		return getBallNumberUrl;
	}

	public void setGetBallNumberUrl(String getBallNumberUrl) {
		this.getBallNumberUrl = getBallNumberUrl;
	}

}
