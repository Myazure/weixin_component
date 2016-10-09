package org.myazure.weixin.repository;

import org.myazure.weixin.domain.MaOfficialAccountWxUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * 
 * @author WangZhen
 *
 */
public interface MaOfficialAccountWxUserRepository extends PagingAndSortingRepository<MaOfficialAccountWxUser, Long> {

    @Query("SELECT a FROM MaOfficialAccountWxUser a LEFT JOIN a.officialAccount oa WHERE oa.id = :oaId and a.openId = :openId")
	MaOfficialAccountWxUser findByOAIdAndWxUserOpenId(@Param("oaId") long oaId, @Param("openId") String openId);

}
