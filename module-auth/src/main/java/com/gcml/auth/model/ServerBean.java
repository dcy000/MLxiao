package com.gcml.auth.model;

public class ServerBean {

    /**
     * serverId : 1
     * serverAccount : test001
     * serverPassword : 123456
     * serverName : 测试一
     * serverLeader : 志强
     * serverPhone : 1111
     */

    private String serverId;
    private String serverAccount;
    private String serverPassword;
    private String serverName;
    private String serverLeader;
    private String serverPhone;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerAccount() {
        return serverAccount;
    }

    public void setServerAccount(String serverAccount) {
        this.serverAccount = serverAccount;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerLeader() {
        return serverLeader;
    }

    public void setServerLeader(String serverLeader) {
        this.serverLeader = serverLeader;
    }

    public String getServerPhone() {
        return serverPhone;
    }

    public void setServerPhone(String serverPhone) {
        this.serverPhone = serverPhone;
    }
}
