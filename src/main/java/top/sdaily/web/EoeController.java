package top.sdaily.web;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.sdaily.core.mybatis.Page;
import top.sdaily.model.Brand;
import top.sdaily.service.BrandService;

import java.util.List;
import java.util.Map;

/**
 * Created by soya on 2016/10/28.
 */
@RestController
@RequestMapping(value = "eoe")
public class EoeController {

    @Autowired
    BrandService brandService;

    @ApiOperation(value="EOE 欢迎", notes="使用示例")
    @RequestMapping(method = RequestMethod.GET)
    public String hiEoe(){
        return "Hi ~~ EOE!!!";
    }

    @ApiOperation(value="测试mybatis", notes="无参查询")
    @RequestMapping(value = "mybatis/query",method = RequestMethod.GET)
    public List<Brand> callMyBatis(){
        return brandService.getAll();
    }


    @ApiOperation(value="测试mybatis", notes="分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "pageNo",value = "页数",required = true),
            @ApiImplicitParam(paramType = "query",name = "pageSize",value = "每页记录数,默认15条",required = false)})
    @RequestMapping(value = "mybatis/page",method = RequestMethod.GET)
    public Page<Brand> callMyBatisWithPage(int pageNo, Integer pageSize){
        Page page = new Page(pageNo,pageSize);
        return brandService.getPage(page);
    }

    @ApiOperation(value="测试mybatis", notes="条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "brandName",value = "品牌名",required = true)})
    @RequestMapping(value = "mybatis/condition",method = RequestMethod.GET)
    public List<Brand> callMyBatisWithCondition(String brandName){
        return brandService.get(brandName);
    }

}
