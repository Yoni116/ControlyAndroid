package net.controly.controly.http.response;

/**
 * This class represents a base response.
 */
public abstract class BaseResponse {

    protected String reason;
    protected String status;

    public BaseResponse() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return Whether the request was successful.
     */
    public boolean hasSucceeded() {
        return status.equals("OK");
    }
}
