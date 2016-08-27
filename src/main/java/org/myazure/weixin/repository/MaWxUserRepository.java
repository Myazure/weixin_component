package org.myazure.weixin.repository;

import org.myazure.weixin.domain.MaWxUser;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface MaWxUserRepository extends PagingAndSortingRepository<MaWxUser, Long> {
	
	MaWxUser findByUnionId(String unionId);
}
