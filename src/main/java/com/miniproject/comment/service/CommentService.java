package com.miniproject.comment.service;

import com.miniproject.comment.domain.Comment;
import com.miniproject.comment.dto.CommentRequestDto;
import com.miniproject.comment.dto.CommentResponseDto;
import com.miniproject.comment.repository.CommentRepository;
import com.miniproject.post.model.Post;
import com.miniproject.post.repository.PostRepository;
import com.miniproject.user.domain.User;
import com.miniproject.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveComment(String username, Long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다.")
        );

        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("다시 로그인해 주세요."));

        String content = commentRequestDto.getContent();
        Comment savecomment = Comment.createComment(user, content, post);

        commentRepository.save(savecomment);
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


    public List<CommentResponseDto> listComment(Long postId) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAllByPostId(postId);

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }
}












