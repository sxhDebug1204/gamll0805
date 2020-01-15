package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.dao.SkuSaleAttrValueDao;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.pms.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Autowired
    private SkuInfoDao skuInfoDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageVo(page);
    }

    /**
     * 商品展示详情页之规格参数值
     * @param spuId
     * @return
     */
    @Override
    public List<SkuSaleAttrValueEntity> querySaleAttrValueBySpuId(Long spuId) {

        //1.根据sku中的spuId查询skus  查询skuInfo表获取spu_id下的所有sku信息
        List<SkuInfoEntity> skuInfoEntities = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        if (CollectionUtils.isEmpty(skuInfoEntities)) {
            return null;
        }

        //2.根据skus获取skuids
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        //3.根据skuIds查询销售属性   根据sku_id查询skuSaleAttrValue表获取sku的值
        return this.list(new QueryWrapper<SkuSaleAttrValueEntity>().in("sku_id", skuIds));
    }

}