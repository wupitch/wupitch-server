package com.server.wupitch.post;

import com.server.wupitch.club.Club;
import com.server.wupitch.club.repository.ClubRepository;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.post.dto.PostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.wupitch.configure.entity.Status.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final ClubRepository clubRepository;
    private final PostRepository postRepository;

    public List<PostRes> getPostListByCrewId(Long crewId) {
        Club club = clubRepository.findByClubIdAndStatus(crewId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        List<Post> list = postRepository.findAllByClubAndStatus(club, VALID);
        return list.stream().map(PostRes::new).collect(Collectors.toList());
    }
}
