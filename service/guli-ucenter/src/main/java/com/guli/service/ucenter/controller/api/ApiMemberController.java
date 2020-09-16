package com.guli.service.ucenter.controller.api;


import com.guli.common.util.FormUtils;
import com.guli.service.base.dto.MemberDto;
import com.guli.service.base.helper.JwtHelper;
import com.guli.service.base.helper.JwtInfo;
import com.guli.service.base.result.R;
import com.guli.service.base.result.ResultCodeEnum;
import com.guli.service.ucenter.entity.form.LoginForm;
import com.guli.service.ucenter.entity.form.RegisterForm;
import com.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "会员管理")
//@CrossOrigin
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;
    //private RedisTemplate<String,String> redisTemplate;

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(
            @ApiParam(value = "注册表单", required = true)
            @RequestBody RegisterForm registerForm){

        String nickname = registerForm.getNickname();
        String mobile = registerForm.getMobile();
        String password = registerForm.getPassword();
        String code = registerForm.getCode();

        ///校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(nickname)) {
            return R.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        //检验验证码
        String key = "checkCode::" + mobile;
        System.out.println("key-------------------- = " + key);
        String checkCode = (String)redisTemplate.opsForValue().get(key);
        //String checkCode = redisTemplate.opsForValue().get(key);
        if(!code.equals(checkCode)){
            // controller层 校验失败 直接返回错误信息
            return R.setResult(ResultCodeEnum.CODE_ERROR);
        }

        // 注册
        memberService.register(registerForm);
        return R.ok().message("注册成功");
    }


    // 登录接口
    @ApiOperation("会员登录")
    @PostMapping("login")
    public R login(@ApiParam(value = "登录表单",required = true)
                   @RequestBody LoginForm loginForm){
                   //LoginForm loginForm){

        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();

        // 参数校验 电话 密码 不能为空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || !FormUtils.isMobile(mobile)){
            return R.setResult(ResultCodeEnum.PARAM_ERROR);
        }

        String token = memberService.login(loginForm);
        return R.ok().data("token",token).message("登录成功");
    }


    /*
        加入微服务网管后，所有auth路径访问的接口 都需要用户登录后 才能被访问
        网关 会对所有 auth路径下的接口进行拦截 并对这些接口进行校验
        从 request 中 获取 header  从header中获取 jwt 校验jwt的有效性
     */
    @ApiOperation("根据token获取登录信息")
    @GetMapping("auth/get-login-info")
    public R getLoginInfo(HttpServletRequest request){
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return R.ok().data("userInfo",jwtInfo);
    }


    @ApiOperation("根据memberId获取用户信息")
    @GetMapping("get-member-dto/{memberId}")
    public R getMemberDtoById(
            @ApiParam(value = "用户id",required = true)
            @PathVariable String memberId){
        MemberDto memberDto = memberService.getMemberDtoById(memberId);
        return R.ok().data("memberDto",memberDto);
    }

}