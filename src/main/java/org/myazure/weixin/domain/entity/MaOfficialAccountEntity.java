package org.myazure.weixin.domain.entity;

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

    private String headImgUrl;

    private boolean authorized = false;

    @NotEmpty
    private String refreshToken;

    public MaOfficialAccountEntity() {

    }

    @Override
	public String toString() {
		return "AdOfficialAccountEntity [id=" + id + ", user=" + user
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
}
