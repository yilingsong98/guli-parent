package com.guli.service.trade.feign.fallback;

import com.guli.service.base.result.R;
import com.guli.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EduCourseServiceFallback implements EduCourseService {
    @Override
    public R getCourseDtoById(String courseId) {
        log.warn("getCourseDtoById熔断保护");
        return R.error();
    }
}
