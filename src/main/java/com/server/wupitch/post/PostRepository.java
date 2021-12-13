package com.server.wupitch.post;

import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{

    List<Post> findAllByClubAndStatusOrderByUpdatedAtDesc(Club club, Status status);

}
