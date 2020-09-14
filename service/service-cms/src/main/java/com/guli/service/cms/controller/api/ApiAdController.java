package com.guli.service.cms.controller.api;

import com.guli.service.base.result.R;
import com.guli.service.cms.entity.Ad;
import com.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

//@CrossOrigin //解决跨域问题
@Api(tags = "广告推荐")
@RestController
@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    private AdService adService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据广告位的id 获取 这个广告位下的所有的广告
     * @param adTypeId
     * @return
     */
    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(@ApiParam(value = "推荐位id", required = true) @PathVariable String adTypeId) {
        // 根据广告位的id 获取 这个广告位下的所有的广告
        List<Ad> ads = adService.selectByAdTypeId(adTypeId);
        // 得到的结果 是 广告集合
        return R.ok().data("items", ads);
    }


    /**
     *
     * redisTemplate.opsForValue(); //操作字符串
     * redisTemplate.opsForHash(); //操作hash
     * redisTemplate.opsForList(); //操作list
     * redisTemplate.opsForSet(); //操作set
     * redisTemplate.opsForZSet(); //操作有序set
     *
     *
     */

    @PostMapping("save-ad")
    public R saveAd(@RequestBody Ad ad){
        // 返回值 ValueOperations 键值对操作工具
        // 默认通过jdk序列化的形式存入redis
        redisTemplate.opsForValue().set("ad",ad);  // 字符串 键值对
        // 存储字符串 更新的时候 需要更新整个ad
        // hash存储 更新的时候 可以只更新单个（id，title，name）字段
        redisTemplate.opsForHash().put("ad","id","123");
        redisTemplate.opsForHash().put("ad","name","zwx");
        redisTemplate.opsForHash().put("ad","title","123456");
        return R.ok();
    }


    @PostMapping("save-code")
    public R saveCode(){
        redisTemplate.opsForValue().set("13264647202","1234",
                5 , TimeUnit.MINUTES);
        return R.ok();
    }


    @DeleteMapping("remove-ad/{key}")
    public R removeAd(@PathVariable String key){
        Boolean delete = redisTemplate.delete(key);
        System.out.println("是否删除成功 = " + delete);
        Boolean hasKey = redisTemplate.hasKey(key);
        System.out.println("key是否存在 = " + hasKey);
        return R.ok();
    }


    @GetMapping("get-code/{key}")
    public R getAd(@PathVariable String key){
        Ad ad = (Ad) redisTemplate.opsForValue().get(key);

        String title = (String)redisTemplate.opsForHash().get("ad", "title");
        String id = (String)redisTemplate.opsForHash().get("ad", "id");
        String name = (String)redisTemplate.opsForHash().get("ad", "name");

        return R.ok().data("ad",ad);
    }

}