package com.guli.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.util.MD5;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.ucenter.entity.Member;
import com.guli.service.ucenter.entity.form.RegisterForm;
import com.guli.service.ucenter.mapper.MemberMapper;
import com.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author donkey
 * @since 2020-09-11
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public void register(RegisterForm registerForm) {

        // 用户是否被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",registerForm.getMobile());
        Integer c = baseMapper.selectCount(queryWrapper);
        if (c > 0) {
            // service层 校验失败 抛出异常
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        // 注册用户
        Member member = new Member();
        member.setMobile(registerForm.getMobile());
        member.setNickname(registerForm.getNickname());
        member.setPassword(MD5.encrypt(registerForm.getPassword()));
        member.setAvatar(
                "https://gili-file-donkey.oss-cn-beijing.aliyuncs.com/avatar/2020-09-06/8ad091df-f649-43d6-9901-1b6813bbaf55.jpg");
        member.setDisabled(false);

        baseMapper.insert(member);
    }
}
