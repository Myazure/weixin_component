package com.ysw.adsense.domain.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.ysw.adsense.domain.AdRole;


public class AdUserEntity {

	@NotEmpty
    private Long id;
	
    @NotEmpty
    private String userName = "";

    private String password = "";

    private String passwordRepeated = "";

    @NotNull
    private AdRole role = AdRole.USER;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public AdRole getRole() {
        return role;
    }

    public void setRole(AdRole role) {
        this.role = role;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
