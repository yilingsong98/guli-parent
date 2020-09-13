package com.guli.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.util.MD5;
import com.guli.service.base.exception.GuliException;
import com.guli.service.base.helper.JwtHelper;
import com.guli.service.base.helper.JwtInfo;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.ucenter.entity.Member;
import com.guli.service.ucenter.entity.form.LoginForm;
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

    // 登录
    @Override
    public String login(LoginForm loginForm) {

        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();

        // 校验手机号
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        // 是否已经存在
        Member member = baseMapper.selectOne(queryWrapper);
        if (member == null){
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        // 校验密码
        if (!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 是否被禁用
        if (member.getDisabled()) {
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 登录成功，生成jwt
        JwtInfo jwtInfo = new JwtInfo(member.getId(), member.getNickname(), member.getAvatar());
        String token = JwtHelper.createToken(jwtInfo);
        return token;
    }

    @Override
    public Member getByOpenid(String openid) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);

        return baseMapper.selectOne(queryWrapper);
    }
}
