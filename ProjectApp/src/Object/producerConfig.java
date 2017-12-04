/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

/**
 *
 * @author linhpn
 */
public class producerConfig {
    private String boostrapServer;
    private String keySerializer;
    private String valueSerializer;
    private String ackConfig;
    private int retryConfig;
    private int batchSizeConfig;
    private int bufferConfig;
    private int lingermsConfig;
    public String getBoostrapServer() {
        return boostrapServer;
    }

    public void setBoostrapServer(String boostrapServer) {
        this.boostrapServer = boostrapServer;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getAckConfig() {
        return ackConfig;
    }

    public void setAckConfig(String ackConfig) {
        this.ackConfig = ackConfig;
    }

    public int getRetryConfig() {
        return retryConfig;
    }

    public void setRetryConfig(int retryConfig) {
        this.retryConfig = retryConfig;
    }

    public int getBatchSizeConfig() {
        return batchSizeConfig;
    }

    public void setBatchSizeConfig(int batchSizeConfig) {
        this.batchSizeConfig = batchSizeConfig;
    }

    public int getBufferConfig() {
        return bufferConfig;
    }

    public void setBufferConfig(int bufferConfig) {
        this.bufferConfig = bufferConfig;
    }

    public int getLingermsConfig() {
        return lingermsConfig;
    }

    public void setLingermsConfig(int lingermsConfig) {
        this.lingermsConfig = lingermsConfig;
    }

   
}
