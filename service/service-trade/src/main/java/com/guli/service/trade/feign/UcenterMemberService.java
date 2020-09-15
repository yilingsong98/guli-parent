package com.guli.service.trade.feign;

import com.guli.service.base.result.R;
import com.guli.service.trade.feign.fallback.UcenterMemberServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallback.class)
public interface UcenterMemberService {

    @GetMapping("/api/ucenter/member/get-member-dto/{memberId}")
    public R getMemberDtoById(@PathVariable(value = "memberId") String memberId);

}
