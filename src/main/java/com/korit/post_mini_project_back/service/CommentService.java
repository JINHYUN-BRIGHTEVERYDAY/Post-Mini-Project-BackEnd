package com.korit.post_mini_project_back.service;

import com.korit.post_mini_project_back.dto.request.CreatePostCommentReqDto;
import com.korit.post_mini_project_back.entity.Comment;
import com.korit.post_mini_project_back.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;

    public void createComment(int postId, CreatePostCommentReqDto dto) {
        commentMapper.insert(Comment.builder()
                        .postId(postId)
                        .parentCommentId(dto.getParentCommentId())
                        .parentUserId(dto.getParentUserId())
                        .content(dto.getContent())
                        .build());
    }
}
