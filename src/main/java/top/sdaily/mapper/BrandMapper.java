package top.sdaily.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.sdaily.core.mybatis.IGenericDao;
import top.sdaily.core.mybatis.annotation.MyBatisRepository;
import top.sdaily.model.Brand;

/**
 * Created by soya on 2016/10/28.
 */
@MyBatisRepository(Brand.class)
@Mapper
public interface BrandMapper extends IGenericDao<Brand,Integer> {

}
