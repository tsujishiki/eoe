package top.sdaily.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.sdaily.core.mybatis.Conditions;
import top.sdaily.core.mybatis.Page;
import top.sdaily.mapper.BrandMapper;
import top.sdaily.model.Brand;
import top.sdaily.service.BrandService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soya on 2016/10/29.
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandMapper brandMapper;

    @Override
    public List<Brand> getAll() {
        return brandMapper.findAll();
    }

    @Override
    public Page<Brand> getPage(Page page) {
        List<Brand> pageResult = brandMapper.find(new Conditions().page(page));
        page.setResults(pageResult);
        return page;
    }

    @Override
    public List<Brand> get(String brandName) {
        System.out.println(brandMapper.findById(1));
        return null;
//        return brandMapper.find(new Conditions().eq("brandname",brandName).addOrderBy("createdate").sortDesc());
    }
}
