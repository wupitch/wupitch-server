package com.server.wupitch.configure.Repository;

import com.server.wupitch.configure.entity.AccountLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLogEntityRepository extends JpaRepository<AccountLogEntity, Long> {
}
