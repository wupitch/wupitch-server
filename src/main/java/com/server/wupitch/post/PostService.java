package com.server.wupitch.post;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import com.server.wupitch.club.accountClubRelation.AccountClubRelationRepository;
import com.server.wupitch.club.repository.ClubRepository;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.post.dto.CreatePostReq;
import com.server.wupitch.post.dto.PostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.server.wupitch.configure.entity.Status.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final ClubRepository clubRepository;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final AccountClubRelationRepository accountClubRelationRepository;

    public List<PostRes> getPostListByCrewId(Long crewId) {
        Club club = clubRepository.findByClubIdAndStatus(crewId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        List<Post> list = postRepository.findAllByClubAndStatusOrderByUpdatedAtDesc(club, VALID);
        return list.stream().map(PostRes::new).collect(Collectors.toList());
    }

    @Transactional
    public void createPostByCrewId(CustomUserDetails customUserDetails, Long crewId, CreatePostReq dto) {

        if(dto.getIsNotice() == null) dto.setIsNotice(false);

        Club club = clubRepository.findByClubIdAndStatus(crewId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Optional<AccountClubRelation> optional = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
        if (optional.isEmpty()) throw new CustomException(CustomExceptionStatus.CREW_NOT_BELONG);
        AccountClubRelation accountClubRelation = optional.get();
        if((accountClubRelation.getIsSelect() == null || !accountClubRelation.getIsSelect())
                && (accountClubRelation.getIsGuest() == null || !accountClubRelation.getIsGuest())) throw new CustomException(CustomExceptionStatus.CREW_NOT_BELONG);

        Post post = new Post(account, club, dto);
        postRepository.save(post);

    }
}
