package org.myazure.weixin.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author WangZhen
 *
 */
public class MaWxUserEntity extends weixin.popular.bean.user.User{

	@NotEmpty
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MaWxUserEntity() {
		super();
	}
}