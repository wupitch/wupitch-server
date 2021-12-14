package com.server.wupitch.post;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import com.server.wupitch.club.accountClubRelation.AccountClubRelationRepository;
import com.server.wupitch.club.dto.CrewResultRes;
import com.server.wupitch.club.repository.ClubRepository;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.post.dto.CreatePostReq;
import com.server.wupitch.post.dto.PostRes;
import com.server.wupitch.post.dto.PostResultRes;
import com.server.wupitch.post.entity.AccountPostRelation;
import com.server.wupitch.post.entity.Post;
import com.server.wupitch.post.repository.AccountPostRelationRepository;
import com.server.wupitch.post.repository.PostRepository;
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
    private final AccountPostRelationRepository accountPostRelationRepository;

    public List<PostRes> getPostListByCrewId(CustomUserDetails customUserDetails,Long crewId) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Club club = clubRepository.findByClubIdAndStatus(crewId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        List<Post> entityList = postRepository.findAllByClubAndStatusAndIsPhotoPostOrderByUpdatedAtDesc(club, VALID, false);
        List<PostRes> list = entityList.stream().map(PostRes::new).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            Post post = entityList.get(i);
            PostRes dto = list.get(i);
            Optional<AccountPostRelation> optional =
                    accountPostRelationRepository.findByAccountAndPostAndStatus(account, post, VALID);
            if(optional.isEmpty()){
                dto.setIsAccountLike(false);
                dto.setIsAccountReport(false);
            }
            else {
                AccountPostRelation accountPostRelation = optional.get();
                dto.setIsAccountLike(accountPostRelation.getIsLike());
                dto.setIsAccountReport(accountPostRelation.getIsReport());
            }
        }
        return list;
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

        Post post = new Post(account, club, dto, false);
        postRepository.save(post);

    }

    @Transactional
    public PostResultRes postLikeToggleByAuth(Long postId, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Post post = postRepository.findByPostIdAndStatus(postId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.POST_NOT_FOUND));

        Optional<AccountPostRelation> optional
                = accountPostRelationRepository.findByAccountAndPostAndStatus(account, post, VALID);
        if(optional.isPresent()){
            optional.get().toggleLike();
            if (optional.get().getIsLike()){
                post.adjustPostLikeCount(true);
                return new PostResultRes(true);
            }
            else{
                post.adjustPostLikeCount(false);
                return new PostResultRes(false);
            }
        }
        else{
            AccountPostRelation build = AccountPostRelation.builder()
                    .status(VALID)
                    .account(account)
                    .post(post)
                    .isLike(true)
                    .isReport(false)
                    .build();
            accountPostRelationRepository.save(build);
            post.adjustPostLikeCount(true);
            return new PostResultRes(true);
        }
    }

    @Transactional
    public PostResultRes postReportToggleByAuth(Long postId, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Post post = postRepository.findByPostIdAndStatus(postId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.POST_NOT_FOUND));

        Optional<AccountPostRelation> optional
                = accountPostRelationRepository.findByAccountAndPostAndStatus(account, post, VALID);
        if(optional.isPresent()){
            optional.get().toggleReport();
            if (optional.get().getIsReport()){
                post.adjustPostReportCount(true);
                return new PostResultRes(true);
            }
            else{
                post.adjustPostReportCount(false);
                return new PostResultRes(false);
            }
        }
        else{
            AccountPostRelation build = AccountPostRelation.builder()
                    .status(VALID)
                    .account(account)
                    .post(post)
                    .isLike(false)
                    .isReport(true)
                    .build();
            accountPostRelationRepository.save(build);
            post.adjustPostReportCount(true);
            return new PostResultRes(true);
        }
    }
}
