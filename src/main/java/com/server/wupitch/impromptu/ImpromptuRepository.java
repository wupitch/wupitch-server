package com.server.wupitch.impromptu;

import com.server.wupitch.impromptu.entity.Impromptu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpromptuRepository extends JpaRepository<Impromptu, Long> {
}
