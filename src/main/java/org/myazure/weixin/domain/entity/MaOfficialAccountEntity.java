package org.myazure.weixin.domain.entity;

import javax.persistence.Column;

import org.hibernate.validator.constraints.NotEmpty;



public class MaOfficialAccountEntity {

	@NotEmpty
    private long id;

	@NotEmpty
    private MaUserEntity user;

	@NotEmpty
    private String appId;

    private String userName;

    private String nickName;
	private String alias;
	private String qrcodeUrl;
	private String headImgUrl;

    private boolean authorized = false;

    @NotEmpty
    private String refreshToken;
   
    
    
    
//    private String service_type_info;
//    private String verify_type_info;
//    private String funcscope_category;
//    private String business_info;
//    
    
    public MaOfficialAccountEntity() {

    }

    @Override
	public String toString() {
		return "MaOfficialAccountEntity [id=" + id + ", user=" + user
				+ ", appId=" + appId + ", userName=" + userName + ", nickName=" + nickName
				+ ", headImgUrl=" + headImgUrl + ", authorized=" + authorized
				+ ", refreshToken=" + refreshToken + "]";
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MaUserEntity getUser() {
        return user;
    }

    public void setUser(MaUserEntity user) {
        this.user = user;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

//	public String getService_type_info() {
//		return service_type_info;
//	}
//
//	public void setService_type_info(String service_type_info) {
//		this.service_type_info = service_type_info;
//	}
//
//	public String getVerify_type_info() {
//		return verify_type_info;
//	}
//
//	public void setVerify_type_info(String verify_type_info) {
//		this.verify_type_info = verify_type_info;
//	}
//
//	public String getFuncscope_category() {
//		return funcscope_category;
//	}
//
//	public void setFuncscope_category(String funcscope_category) {
//		this.funcscope_category = funcscope_category;
//	}
//
//	public String getBusiness_info() {
//		return business_info;
//	}
//
//	public void setBusiness_info(String business_info) {
//		this.business_info = business_info;
//	}
}
