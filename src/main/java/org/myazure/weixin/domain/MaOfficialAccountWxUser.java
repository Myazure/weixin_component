package org.myazure.weixin.domain;

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
@Table(name = "myazure_weixin_official_account_wx_user")
public class MaOfficialAccountWxUser extends BaseEntity {

	@Id
	@Column(name = "official_account_wx_user_id", unique = true, nullable = false, columnDefinition = "INT")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(targetEntity = MaOfficialAccount.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "official_account_id")
	private MaOfficialAccount officialAccount;
	
	@OneToOne(targetEntity = MaWxUser.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "wx_user_id")
	private MaWxUser wxUser;
	
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

	public MaOfficialAccountWxUser() {
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

	public MaOfficialAccount getOfficialAccount() {
		return officialAccount;
	}

	public void setOfficialAccount(MaOfficialAccount officialAccount) {
		this.officialAccount = officialAccount;
	}

	public MaWxUser getWxUser() {
		return wxUser;
	}

	public void setWxUser(MaWxUser wxUser) {
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