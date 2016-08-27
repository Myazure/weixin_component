package com.ysw.adsense.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ysw_ad_official_account_wx_user")
public class AdOfficialAccountWxUser extends BaseEntity {

	@Id
	@Column(name = "official_account_wx_user_id", unique = true, nullable = false, columnDefinition = "INT")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(targetEntity = AdOfficialAccount.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "official_account_id")
	private AdOfficialAccount officialAccount;
	
	@OneToOne(targetEntity = AdWxUser.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "wx_user_id")
	private AdWxUser wxUser;
	
	@Column(name = "open_id")
	private String openId;

    @Column(name = "subscribed", columnDefinition = "BIT", length = 1)
    private boolean subscribed = false;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "subscribed_time")
	private Date subscribedTime;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "groupid")
	private int groupid;
	
	@Column(name = "tagid_list")
	private String tagidList;

	public AdOfficialAccountWxUser() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getTagidList() {
		return tagidList;
	}

	public void setTagidList(String tagidList) {
		this.tagidList = tagidList;
	}

	public AdOfficialAccount getOfficialAccount() {
		return officialAccount;
	}

	public void setOfficialAccount(AdOfficialAccount officialAccount) {
		this.officialAccount = officialAccount;
	}

	public AdWxUser getWxUser() {
		return wxUser;
	}

	public void setWxUser(AdWxUser wxUser) {
		this.wxUser = wxUser;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Date getSubscribedTime() {
		return subscribedTime;
	}

	public void setSubscribedTime(Date subscribedTime) {
		this.subscribedTime = subscribedTime;
	}

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
}