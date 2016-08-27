package org.myazure.weixin.configuration;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author csyangchsh@gmail.com
 */
public class AppUrlService {

    @Value("${adsense.appUrl}")
    private String appUrl;
    @Value("${adsense.ipUrl}")
    private String ipUrl;

    public AppUrlService() {

    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getIpUrl() {
        return ipUrl;
    }

    public void setIpUrl(String ipUrl) {
        this.ipUrl = ipUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
