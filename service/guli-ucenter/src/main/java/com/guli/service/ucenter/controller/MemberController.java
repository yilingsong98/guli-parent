package com.guli.service.ucenter.controller;


import com.guli.service.base.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author donkey
 * @since 2020-09-11
 */
@RestController
@RequestMapping("/ucenter/member")
public class MemberController {

    @GetMapping("test")
    public R test(){
        return R.ok().message("test");
    }
}

