package org.myazure.weixin.configuration;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author WangZhen
 */
public class AppUrlService {

    @Value("${myazure.appUrl}")
    private String appUrl;

    public AppUrlService() {

    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
