package top.sdaily.model;

import top.sdaily.core.mybatis.annotation.MyBatisColumn;
import top.sdaily.core.mybatis.annotation.MybatisID;
import top.sdaily.core.mybatis.annotation.MybatisTable;

@MybatisTable("bm_brand")
public class Brand{

    @MyBatisColumn(value = "brandid",isID = true, isAutoKey = false)
    private int brandId;
    private String brandIcon;

    private String brandName;

    private String intro;

    private int companyId;

    private int brandType;


    private int promPlace;


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getBrandType() {
        return brandType;
    }

    public void setBrandType(int brandType) {
        this.brandType = brandType;
    }

    public int getPromPlace() {
        return promPlace;
    }

    public void setPromPlace(int promPlace) {
        this.promPlace = promPlace;
    }

    public String getBrandIcon() {
        return brandIcon;
    }

    public void setBrandIcon(String brandIcon) {
        this.brandIcon = brandIcon;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

}
