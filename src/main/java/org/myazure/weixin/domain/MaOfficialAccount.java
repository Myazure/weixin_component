package org.myazure.weixin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "myazure_weixin_official_account")
public class MaOfficialAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "official_account_id")
    private long id;

    @OneToOne(targetEntity = MaUser.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private MaUser user;

    @Column(name  = "app_id")
    private String appId;
    
    @Column(name  = "username")
    private String userName;
    
    @Column(name = "alias")
	private String alias;

	@Column(name = "nickname")
	private String nickName;
	
	@Column(name = "qrcode_url")
	private String qrcodeUrl;
    
    @Column(name  = "head_img_url")
    private String headImgUrl;

    @Column(name = "authorized", columnDefinition = "BIT", length = 1)
    private boolean authorized = false;

    @Column(name = "refresh_token")
    private String refreshToken;

    public MaOfficialAccount() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MaUser getUser() {
        return user;
    }

    public void setUser(MaUser user) {
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
}
