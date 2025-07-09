package com.app.invordersys.models;

import java.sql.Timestamp;

public class RestockRequest {
    private int restockId;
    private int productId;
    private int quantityRequested;
    private Timestamp requestedDate;
    private String status;

    public RestockRequest() {}

    public RestockRequest(int restockId, int productId, int quantityRequested, Timestamp requestedDate, String status) {
        this.restockId = restockId;
        this.productId = productId;
        this.quantityRequested = quantityRequested;
        this.requestedDate = requestedDate;
        this.status = status;
    }

    public int getRestockId() {
        return restockId;
    }

    public void setRestockId(int restockId) {
        this.restockId = restockId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public Timestamp getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Timestamp requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RestockRequest{" +
                "restockId=" + restockId +
                ", productId=" + productId +
                ", quantityRequested=" + quantityRequested +
                ", requestedDate=" + requestedDate +
                ", status='" + status + '\'' +
                '}';
    }
}