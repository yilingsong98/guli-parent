package com.guli.service.trade.feign.fallback;

import com.guli.service.base.result.R;
import com.guli.service.trade.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UcenterMemberServiceFallback implements UcenterMemberService {
    @Override
    public R getMemberDtoById(String memberId) {
        log.warn("getMemberDtoById的熔断保护");
        return R.error();
    }
}
