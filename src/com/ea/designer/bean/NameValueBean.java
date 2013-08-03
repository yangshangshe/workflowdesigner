package com.ea.designer.bean;

/**
 * ****************************************************************************
 * 
 * @function：
 * @author yss
 * @file_name NameValueBean.java
 * @package_name：com.ea.designer.bean
 * @project_name：WorkFlowDesigner
 * 
 * 
 * ****************************************************************************
 * 修改人 修改时间 修改内容
 * 
 * ****************************************************************************
 */
public class NameValueBean {

    private String name;
    private String value;

    public NameValueBean() {

    }

    public NameValueBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (null == o)
            return false;
        if (o instanceof NameValueBean) {
            if (((NameValueBean) o).getValue().equals(getValue())) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this.name;
    }
}
