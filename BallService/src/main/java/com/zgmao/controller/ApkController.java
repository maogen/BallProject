package com.zgmao.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController@RequestMapping("/api/apk")
public class ApkController {

	// @Autowired
	// private WebUrl webUrl;

	@Value("${apk_version}")
	private String apkVerion;

	@RequestMapping("getApkVersion")
	@Transactional
	public String getApkVersion() {
		return apkVerion;
	}

	// @RequestMapping("getWebUrl")
	// public String getWebUrl() {
	// return webUrl.getGetBallNumberUrl();
	// }
}
