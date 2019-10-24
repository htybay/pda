package com.chicv.pda.bean;

/**
 * author: liheyu
 * date: 2019-10-23
 * email: liheyu999@163.com
 */
public class PermissionBean {


    /**
     * ParentId : 0
     * Name : 采购模块
     * PermissionGuid : 60b17899-f48a-4273-8082-89ed57104c0a
     * Description : null
     * Icon : icon-shopping-cart
     * CanMenu : true
     * Href : #
     * IsEnable : true
     * Sort : 18
     * Id : 9
     * CreateUserName : 管理员
     * CreateTime : 2015-04-15T20:49:32
     * UpdateUserName : 管理员
     * UpdateTime : 2015-04-15T20:55:57.203
     */

    private int ParentId;
    private String Name;
    private String PermissionGuid;
    private String Description;
    private String Icon;
    private boolean CanMenu;
    private String Href;
    private boolean IsEnable;
    private int Sort;
    private int Id;
    private String CreateUserName;
    private String CreateTime;
    private String UpdateUserName;
    private String UpdateTime;

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int ParentId) {
        this.ParentId = ParentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPermissionGuid() {
        return PermissionGuid;
    }

    public void setPermissionGuid(String PermissionGuid) {
        this.PermissionGuid = PermissionGuid;
    }

    public Object getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public boolean isCanMenu() {
        return CanMenu;
    }

    public void setCanMenu(boolean CanMenu) {
        this.CanMenu = CanMenu;
    }

    public String getHref() {
        return Href;
    }

    public void setHref(String Href) {
        this.Href = Href;
    }

    public boolean isIsEnable() {
        return IsEnable;
    }

    public void setIsEnable(boolean IsEnable) {
        this.IsEnable = IsEnable;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String CreateUserName) {
        this.CreateUserName = CreateUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getUpdateUserName() {
        return UpdateUserName;
    }

    public void setUpdateUserName(String UpdateUserName) {
        this.UpdateUserName = UpdateUserName;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
}
