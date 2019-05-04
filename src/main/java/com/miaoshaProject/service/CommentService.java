package com.miaoshaProject.service;


import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.CommentModel;

import java.util.List;

public interface CommentService {
    //添加评论
    void addComment(Integer userId,Integer itemId,String content) throws BusinessException;
    //主要评论展示
    List<CommentModel> listComment(Integer itemId);
}
