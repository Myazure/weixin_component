package com.ysw.adsense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ysw.adsense.domain.AdOfficialAccount;


public interface AdOfficialAccountRepository extends PagingAndSortingRepository<AdOfficialAccount, Long> {

    List<AdOfficialAccount> findByAppId(String appId);

    @Query("SELECT oa FROM AdOfficialAccount oa LEFT JOIN oa.user u WHERE u.userName = :name")
    List<AdOfficialAccount> findByUser(@Param("name") String userName);

    AdOfficialAccount findByUserName(String userName);
}
