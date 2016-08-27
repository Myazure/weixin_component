package org.myazure.weixin.repository;

import java.util.List;

import org.myazure.weixin.domain.MaOfficialAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface MaOfficialAccountRepository extends PagingAndSortingRepository<MaOfficialAccount, Long> {

    List<MaOfficialAccount> findByAppId(String appId);

    @Query("SELECT oa FROM MaOfficialAccount oa LEFT JOIN oa.user u WHERE u.userName = :name")
    List<MaOfficialAccount> findByUser(@Param("name") String userName);

    MaOfficialAccount findByUserName(String userName);
}
