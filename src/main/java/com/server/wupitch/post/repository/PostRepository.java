package com.server.wupitch.post.repository;

import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{

    List<Post> findAllByClubAndStatusAndIsPhotoPostOrderByUpdatedAtDesc(Club club, Status status, Boolean isPhotoPost);
    Optional<Post> findByPostIdAndStatus(Long postId, Status status);

}
