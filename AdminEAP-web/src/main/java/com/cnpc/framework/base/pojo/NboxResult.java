package com.cnpc.framework.base.pojo;

/**
 * @author bill
 *
 */
public class NboxResult {

    /**
     * 执行结果
     */
    private boolean success;

    /**
     * 结果集
     */
    private Object data;

    /**
     * 返回信息
     */
    private String successMsg;

    /**
     * 返回状态码
     */
    private String statusCode;

    public NboxResult() {
        this.successMsg ="操作成功";
        this.statusCode ="0000";
        this.success = true;
    }

    public NboxResult(boolean success) {

        this.success = success;
    }

    public NboxResult(boolean success, Object data) {

        this.success = success;
        this.data = data;
    }

    public NboxResult(boolean success, Object data, String message) {

        this.success = success;
        this.data = data;
        this.successMsg = message;
    }

    public NboxResult(boolean success, Object data, String message, String statusCode) {

        this.success = success;
        this.data = data;
        this.successMsg = message;
        this.statusCode = statusCode;
    }

    public NboxResult(boolean success, String message) {

        this.success = success;
        this.successMsg = message;
    }

    public NboxResult(boolean success, String message ,String statusCode) {

        this.success = success;
        this.successMsg = message;
        this.statusCode = statusCode;
    }
    
    public NboxResult(String statusCode, String message) {

        this.statusCode = statusCode;
        this.successMsg = message;
    }

    public NboxResult(ResultCode rc) {

        this.statusCode = rc.getCode();
        this.successMsg = rc.getMessage();
    }

    
    public boolean isSuccess() {
    
        return success;
    }

    
    public void setSuccess(boolean success) {
    
        this.success = success;
    }

    
    public Object getData() {
    
        return data;
    }

    
    public void setData(Object data) {
    
        this.data = data;
    }

    
    public String getSuccessMsg() {
    
        return successMsg;
    }

    
    public void setSuccessMsg(String successMsg) {
    
        this.successMsg = successMsg;
    }

    
    public String getStatusCode() {
    
        return statusCode;
    }

    
    public void setStatusCode(String statusCode) {
    
        this.statusCode = statusCode;
    }


}
