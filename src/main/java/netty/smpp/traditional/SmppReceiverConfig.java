package netty.smpp.traditional;

public class SmppReceiverConfig {

    private String host = "localhost";

    private int port = 1234;

    private String systemId = "DAMIEN";

    private String password = "neimad";

    private String systemType = "";

    private int bindTON = 0;

    private int bindNPI = 0;

    private String addressRange = "1000";


    /*

sourceTON=0
sourceNPI=0
destinationTON=0
destinationNPI=0
enquireLinkPause=30000
maximumTimesWaitForEnquireLinkResponse=60000
timesWaitForUnbindResponse=1000
checkFilestatusThreadSleep=2000
deadThreadSleep=5000
restartInMinute=30
     */

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public int getBindTON() {
        return bindTON;
    }

    public void setBindTON(int bindTON) {
        this.bindTON = bindTON;
    }

    public int getBindNPI() {
        return bindNPI;
    }

    public void setBindNPI(int bindNPI) {
        this.bindNPI = bindNPI;
    }

    public String getAddressRange() {
        return addressRange;
    }

    public void setAddressRange(String addressRange) {
        this.addressRange = addressRange;
    }
}
