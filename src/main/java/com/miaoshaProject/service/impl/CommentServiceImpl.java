package com.miaoshaProject.service.impl;

import com.miaoshaProject.dao.CommentDOMapper;
import com.miaoshaProject.dataobject.CommentDO;
import com.miaoshaProject.dataobject.ItemStockDO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.service.CommentService;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.ValidateService;
import com.miaoshaProject.service.model.CommentModel;
import com.miaoshaProject.service.model.ItemModel;
import com.miaoshaProject.service.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ValidateService validateService;

    @Autowired
    private CommentDOMapper commentDOMapper;

    @Autowired
    private UserService userService;

    @Override
    public void addComment(Integer userId, Integer itemId, String content) throws BusinessException {
        validateService.userValidate(userId);
        validateService.itemValidate(itemId);
        CommentModel commentModel = new CommentModel();
        commentModel.setUserId(userId);
        commentModel.setItemId(itemId);
        if(content == null || content.length() == 0){
            throw new BusinessException(EmBusinessError.COMMENT_IS_NULL);
        }else{
            commentModel.setContent(content);
        }
        commentModel.setCreateTime(new DateTime());
        CommentDO commentDO = this.convertDOFromModel(commentModel);
        commentDOMapper.insertSelective(commentDO);

    }

    @Override
    public List<CommentModel> listComment(Integer itemId) {
        List<CommentDO> commentDOList = commentDOMapper.selectByItemId(itemId);
        List<CommentModel> commentModelList = commentDOList.stream().map(commentDO -> {
            CommentModel commentModel = this.convertModelFromDO(commentDO);
            UserModel userModel = userService.getUserById(commentDO.getUserId());
            commentModel.setUserName(userModel.getName());
            return commentModel;
        }).collect(Collectors.toList());
        return commentModelList;
    }

    private CommentDO convertDOFromModel(CommentModel commentModel){
        if(commentModel == null){
            return null;
        }
        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(commentModel,commentDO);
        commentDO.setCreateTime(commentModel.getCreateTime().toDate());
        return commentDO;
    }
    private CommentModel convertModelFromDO(CommentDO commentDO){
        if(commentDO == null){
            return null;
        }
        CommentModel commentModel = new CommentModel();
        BeanUtils.copyProperties(commentDO,commentModel);
        commentModel.setCreateTime(new DateTime(commentDO.getCreateTime()));
        return commentModel;
    }
}
