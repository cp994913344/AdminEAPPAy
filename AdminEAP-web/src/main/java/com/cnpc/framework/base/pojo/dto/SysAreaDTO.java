package com.cnpc.framework.base.pojo.dto;

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
public class SysAreaDTO {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;


    /**
     * 级别
     */
    private String level;
    
    /**
     * 上级id
     */
    private String parentCode;

    private List<SysAreaDTO> children;

    
    public String getCode() {
    
        return code;
    }

    
    public void setCode(String code) {
    
        this.code = code;
    }

    
    public String getName() {
    
        return name;
    }

    
    public void setName(String name) {
    
        this.name = name;
    }

    
    public String getLevel() {
    
        return level;
    }

    
    public void setLevel(String level) {
    
        this.level = level;
    }

    
    public List<SysAreaDTO> getChildren() {
    
        return children;
    }

    
    public void setChildren(List<SysAreaDTO> children) {
    
        this.children = children;
    }


    
    public String getParentCode() {
    
        return parentCode;
    }


    
    public void setParentCode(String parentCode) {
    
        this.parentCode = parentCode;
    }
    
}
