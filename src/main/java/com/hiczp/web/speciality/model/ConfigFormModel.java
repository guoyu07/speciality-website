package com.hiczp.web.speciality.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * Created by czp on 17-5-13.
 */
@Component
public class ConfigFormModel {
    @NotEmpty(message = "学校名不可为空")
    private String universityName;

    @NotEmpty(message = "专业名称不可为空")
    private String specialityName;

    private String copyright;

    private String icp;

    private Integer[] navbarSorts;

    private Integer mainIndexSort;

    private Integer[] indexSorts;

    private String[] carouselImages;

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public Integer[] getNavbarSorts() {
        return navbarSorts;
    }

    public void setNavbarSorts(Integer[] navbarSorts) {
        this.navbarSorts = navbarSorts;
    }

    public Integer getMainIndexSort() {
        return mainIndexSort;
    }

    public void setMainIndexSort(Integer mainIndexSort) {
        this.mainIndexSort = mainIndexSort;
    }

    public Integer[] getIndexSorts() {
        return indexSorts;
    }

    public void setIndexSorts(Integer[] indexSorts) {
        this.indexSorts = indexSorts;
    }

    public String[] getCarouselImages() {
        return carouselImages;
    }

    public void setCarouselImages(String[] carouselImages) {
        this.carouselImages = carouselImages;
    }
}
