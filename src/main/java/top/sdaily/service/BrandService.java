package top.sdaily.service;

import top.sdaily.core.mybatis.Page;
import top.sdaily.model.Brand;

import java.util.List;
import java.util.Map;

/**
 * Created by soya on 2016/10/29.
 */
public interface BrandService {

    List<Brand> getAll();

    Page<Brand> getPage(Page page);

    List<Brand> get(String brandName);

}
