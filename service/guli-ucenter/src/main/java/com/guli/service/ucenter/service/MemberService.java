package com.guli.service.ucenter.service;

import com.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.ucenter.entity.form.RegisterForm;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-09-11
 */
public interface MemberService extends IService<Member> {

    void register(RegisterForm registerForm);

}
