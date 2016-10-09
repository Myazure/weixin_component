package org.myazure.weixin.domain.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.myazure.weixin.domain.MaRole;

/**
 * 
 * @author WangZhen
 *
 */
public class MaUserEntity {

	@NotEmpty
    private Long id;
	
    @NotEmpty
    private String userName = "";

    private String password = "";

    private String passwordRepeated = "";

    @NotNull
    private MaRole role = MaRole.USER;

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

    public MaRole getRole() {
        return role;
    }

    public void setRole(MaRole role) {
        this.role = role;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
