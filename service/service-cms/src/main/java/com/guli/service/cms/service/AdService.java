package com.guli.service.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.service.cms.entity.vo.AdVo;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author donkey
 * @since 2020-09-10
 */
public interface AdService extends IService<Ad> {

    void selectPage(Page<AdVo> pageParam);

    boolean removeAdImageById(String id);

    List<Ad> selectByAdTypeId(String adTypeId);
}
