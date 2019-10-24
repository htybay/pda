package com.chicv.pda.bean;


import java.util.List;

public class User {

    private String name;
    private String loginAccount;
    private String id;
    private String email;
    private String pwd;
    private String accessToken;
    private int RoomId;
    private String RoomName;
    private List<PermissionBean> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public List<PermissionBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionBean> permissions) {
        this.permissions = permissions;
    }

    public boolean containPermission(String permissionName) {
        if (permissions == null || permissionName == null) {
            return false;
        }
        for (PermissionBean permission : permissions) {
            if (permissionName.equalsIgnoreCase(permission.getPermissionGuid())) {
                return true;
            }
        }
        return false;
    }
}
