package com.korit.post_mini_project_back.service;

import com.korit.post_mini_project_back.dto.request.CreatePostReqDto;
import com.korit.post_mini_project_back.entity.ImageFile;
import com.korit.post_mini_project_back.entity.Post;
import com.korit.post_mini_project_back.mapper.ImageFileMapper;
import com.korit.post_mini_project_back.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final FileService fileService;
    private final PostMapper postMapper;
    private final ImageFileMapper imageFileMapper;


    @Transactional(rollbackFor = Exception.class)
    public void createPost(CreatePostReqDto dto) {
        List<ImageFile> files = fileService.upload("post", dto.getFiles());
        Post post = dto.toEntity();
        postMapper.insert(post);
        if (files == null) { // 이미지가 없는 경우를 의미 -> 이미지가 없는 경우에도 게시글 작성이 되도록
            return;
        }
        files.forEach(file -> file.setReferenceId(post.getPostId()));
        imageFileMapper.insertToMany(files);
    }

}

