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

    
    
    
    
    //add 2016-10-09
//    
//    @Column(name = "service_type_info")
//    private String service_type_info;
//    @Column(name = "verify_type_info")
//    private String verify_type_info;
//    @Column(name = "funcscope_category")
//    private String funcscope_category;
//    @Column(name = "business_info")
//    private String business_info;
    
//    info.getAuthorization_info().getFunc_info();
//	info.getAuthorization_info().getAppid();
//	info.getAuthorizer_info().getAlias();
//	info.getAuthorizer_info().getBusiness_info();
//	info.getAuthorizer_info().getHead_img();
//	info.getAuthorizer_info().getNick_name();
//	info.getAuthorizer_info().getQrcode_url();
//	info.getAuthorizer_info().getService_type_info();
//	info.getAuthorizer_info().getUser_name();
//	info.getAuthorizer_info().getVerify_type_info();
	
    
    
    
    
    
    
    
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
