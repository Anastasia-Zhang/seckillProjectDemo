package com.miaoshaProject.controllor;

import com.miaoshaProject.controllor.viewobj.CommentVO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.reponse.CommonReturnType;
import com.miaoshaProject.service.CommentService;
import com.miaoshaProject.service.model.CommentModel;
import com.miaoshaProject.service.model.UserModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller("comment")
@RequestMapping("/comment")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class CommentController extends BaseController{

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/addComment",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType addAddress(@RequestParam(name = "itemId") Integer itemId,
                                       @RequestParam(name = "content") String content) throws BusinessException{
        UserModel userModel = validateUserLogin();
        commentService.addComment(userModel.getId(),itemId,content);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/listComment",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listComment(@RequestParam(name = "itemId") Integer itemId){
        List<CommentModel> commentModelList = commentService.listComment(itemId);
        List<CommentVO> commentVOList = commentModelList.stream().map(commentModel -> {
            CommentVO commentVO = this.convertFromModelToVO(commentModel);
            return commentVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(commentVOList);
    }

    private CommentVO convertFromModelToVO(CommentModel commentModel){
        if(commentModel == null){
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(commentModel,commentVO);
        commentVO.setCreateTime(commentModel.getCreateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        return commentVO;
    }
}
