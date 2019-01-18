package com.gcml.common.server;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/1/17.
 */

public class ServerBean implements Serializable {

    /**
     * serverId : 1
     * serverAccount : test001
     * serverPassword : 123456
     * serverName : 大青蛙医院
     * serverLeader : 志强
     * serverPhone : 1111
     * doctorLogin : 1
     * userRegister : 1,2,3,4
     * userLogin : 1,2,3,4
     */

    public int serverId;
    public String serverAccount;
    public String serverPassword;
    public String serverName;
    public String serverLeader;
    public String serverPhone;
    public String doctorLogin;
    public String userRegister;
    public String userLogin;
}
