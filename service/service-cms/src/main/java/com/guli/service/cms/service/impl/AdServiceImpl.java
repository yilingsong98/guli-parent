package com.guli.service.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.service.base.result.R;
import com.guli.service.cms.entity.Ad;
import com.guli.service.cms.entity.vo.AdVo;
import com.guli.service.cms.feign.OssFileService;
import com.guli.service.cms.mapper.AdMapper;
import com.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author donkey
 * @since 2020-09-10
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private OssFileService ossFileService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void selectPage(Page<AdVo> pageParam) {
        QueryWrapper<AdVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("a.type_id", "a.sort");
        List<AdVo> records = baseMapper.selectPageByQueryWrapper(pageParam, queryWrapper);
        pageParam.setRecords(records);
    }


    /*
        根据id删除 幻灯片图片
     */
    @Override
    public boolean removeAdImageById(String id) {
        // 根据id拿到幻灯片图片
        Ad ad = baseMapper.selectById(id);

        if (ad != null){
            String imageUrl = ad.getImageUrl();

            // 判断地址是否存在
            if (!StringUtils.isEmpty(imageUrl)) {
                // 删除图片： 调用client方法
                R r = ossFileService.removeFile(imageUrl);
                return r.getSuccess();
            }
        }

        return false;
    }

    /**
     * 缓存 同步的方案
     *  1. 实时性高的时候  增删改的时候 将查询的数据的时候 更新缓存
     *  2. 实时性要求不高的时候 定时清除缓存 设置缓存的过期时间
     * @param adTypeId
     * @return
     */
   // @Override
    public List<Ad> selectByAdTypeId1(String adTypeId) {

        // 缓存如果挂了 直接跳过 到数据库中取数据
        // 缓存服务器挂了 直接吞掉
        List<Ad> adList = null;
        try {
            adList = (List<Ad>) redisTemplate.opsForValue().get(adTypeId);
            // 判断缓存中 是否有数据
            if (adList != null ){
                // 有 则将数据从缓存中返回
                return adList;
            }
        } catch (Exception e) {
            log.warn("redis 服务器异常： index：：adList");
        }

        // 如果没有 则从数据库中 获取
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        // 广告排序
        queryWrapper.orderByAsc("sort", "id");
        // 根据id找到广告
        queryWrapper.eq("type_id", adTypeId);
        adList = baseMapper.selectList(queryWrapper);

        try {
            // 将查询到的数据 存入缓存中
            redisTemplate.opsForValue().set("index::adList",adList,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("redis 服务器异常： index：：adList");
        }

        return adList;
    }

    @Cacheable(value = "index",key = "'selectByAdTypeId'")
    @Override
    public List<Ad> selectByAdTypeId(String adTypeId) {

        // 如果没有 则从数据库中 获取
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        // 广告排序
        queryWrapper.orderByAsc("sort", "id");
        // 根据id找到广告
        queryWrapper.eq("type_id", adTypeId);
        List<Ad> adList = baseMapper.selectList(queryWrapper);

        return adList;
    }
}
