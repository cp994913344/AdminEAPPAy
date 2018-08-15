package com.cnpc.framework.base.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 地区表信息
 * 
 * @author
 *
 */
@Entity
@Table(name = "tbl_sys_area")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class SysArea extends BaseEntity {

    /**
     *
     */  
    private static final long serialVersionUID = -2985770613730537447L;

    /**
     * 编号
     */
    @Column(name = "CODE", length = 100)
    private String code;

    /**
     * 名称
     */
    @Column(name = "NAME", length = 100)
    private String name;

    /**
     * 市级首字母
     */
    @Column(name = "LETTER", length = 100)
    private String letter;

    /**
     * 级别
     */
    @Column(name = "LEVEL", length = 100)
    private String level;

    /**
     * 拼音
     */
    @Column(name = "PIN_YIN", length = 100)
    private String pinyin;

    /**
     * 全称
     */
    @Column(name = "MERGER_NAME", length = 100)
    private String mergerName;

    /**
     * 经度
     */
    @Column(name = "ING", length = 100)
    private String ing;

    /**
     * 纬度
     */
    @Column(name = "LAT", length = 100)
    private String lat;

    @Transient
    private boolean isCheck;
    


    /**
     * @return the code
     */
    public String getCode() {

        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * @return the letter
     */
    public String getLetter() {

        return letter;
    }

    /**
     * @param letter
     *            the letter to set
     */
    public void setLetter(String letter) {

        this.letter = letter;
    }

    /**
     * @return the level
     */
    public String getLevel() {

        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(String level) {

        this.level = level;
    }

    /**
     * @return the pinyin
     */
    public String getPinyin() {

        return pinyin;
    }

    /**
     * @param pinyin
     *            the pinyin to set
     */
    public void setPinyin(String pinyin) {

        this.pinyin = pinyin;
    }

    /**
     * @return the ing
     */
    public String getIng() {

        return ing;
    }

    /**
     * @param ing
     *            the ing to set
     */
    public void setIng(String ing) {

        this.ing = ing;
    }

    /**
     * @return the lat
     */
    public String getLat() {

        return lat;
    }

    /**
     * @param lat
     *            the lat to set
     */
    public void setLat(String lat) {

        this.lat = lat;
    }

    /**
     * @return the isCheck
     */
    public boolean isCheck() {

        return isCheck;
    }

    /**
     * @param isCheck
     *            the isCheck to set
     */
    public void setCheck(boolean isCheck) {

        this.isCheck = isCheck;
    }

    /**
     * @return the mergerName
     */
    public String getMergerName() {

        return mergerName;
    }

    /**
     * @param mergerName
     *            the mergerName to set
     */
    public void setMergerName(String mergerName) {

        this.mergerName = mergerName;
    }

}
