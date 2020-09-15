package com.guli.service.trade.feign;


import com.guli.service.base.result.R;
import com.guli.service.trade.feign.fallback.EduCourseServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-edu",fallback = EduCourseServiceFallback.class)
public interface EduCourseService {

    @GetMapping("/api/edu/course/get-course-dto/{courseId}")
    public R getCourseDtoById(@PathVariable(value = "courseId") String courseId);
}
