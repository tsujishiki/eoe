package top.sdaily.core.mybatis;

import java.util.HashMap;

/**
 * 查询条件支持类
 * Created by soya on 2016/10/29.
 */
public class Conditions<String,Object> extends HashMap {

    public Conditions(){
        super();
    }

    /**
     * a = b
     * @param key
     * @param value
     * @return
     */
    public Conditions<String,Object> eq(String key,Object value){
        this.put("_eq_" + key,value);
        return this;
    }

    /**
     * a > b
     * @param columnA
     * @param columnB
     * @return
     */
    public Conditions<String,Object> gt(String columnA,String columnB){
        this.put("_gt_" + columnA,columnB);
        return  this;
    }

    /**
     * a >= b
     * @param columnA
     * @param columnB
     * @return
     */
    public Conditions<String ,Object> gte(String columnA,String columnB){
        this.put("_gte_" + columnA,columnB);
        return  this;
    }

    /**
     * a < b
     * @param columnA
     * @param columnB
     * @return
     */
    public Conditions<String,Object> lt(String columnA,String columnB){
        this.put("_lt_" + columnA,columnB);
        return  this;
    }


    /**
     * a <= b
     * @param columnA
     * @param columnB
     * @return
     */
    public Conditions<String ,Object> lte(String columnA,String columnB){
        this.put("_lte_" + columnA,columnB);
        return  this;
    }

    /**
     * columnA like '%value%'
     * @param columnA
     * @param value
     * @return
     */
    public Conditions<String ,Object> like(String columnA,String value){
        this.put("_like_" + columnA,value);
        return  this;
    }

    /**
     * columnA in (value..)
     * @param columnA
     * @param value
     * @return
     */
    public Conditions<String ,Object> in(String columnA,Object... value){
        this.put("_in_" + columnA,value);
        return  this;
    }


    /**
     * order by
     * @param column
     * @return
     */
    public Conditions<String,Object> addOrderBy(String column){
        this.put("_order_by",column);
        return this;
    }

    /**
     * desc
     * @return
     */
    public Conditions<String,Object> sortDesc(){
        this.put("_sort_rule","desc");
        return this;
    }

    /**
     * limit ...
     * @param page
     * @return
     */
    public Conditions<String,Object> page(Page page){
        this.put("_page",page);
        return this;
    }

}
