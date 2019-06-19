package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-06-19
 * email: liheyu999@163.com
 */
public class UpdateInfo {

    //最新版本号
    private int versionCode;
    //最新版本下载路径
    private String updateUrl;
    //版本是否强制更新：0-否，1-是
    private String isUpdate;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }
}
