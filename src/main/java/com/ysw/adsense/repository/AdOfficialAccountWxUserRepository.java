package com.ysw.adsense.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ysw.adsense.domain.AdOfficialAccountWxUser;


public interface AdOfficialAccountWxUserRepository extends PagingAndSortingRepository<AdOfficialAccountWxUser, Long> {

    @Query("SELECT a FROM AdOfficialAccountWxUser a LEFT JOIN a.officialAccount oa WHERE oa.id = :oaId and a.openId = :openId")
	AdOfficialAccountWxUser findByOAIdAndWxUserOpenId(@Param("oaId") long oaId, @Param("openId") String openId);

}
