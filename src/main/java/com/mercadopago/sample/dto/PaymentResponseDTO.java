package com.mercadopago.sample.dto;

public class PaymentResponseDTO {
    private Long id;
    private String status;
    private String detail;
    private String external_resource_url;
    private String creq;

    public PaymentResponseDTO(Long id, String status, String detail, String external_resource_url, String creq) {
        this.id = id;
        this.status = status;
        this.detail = detail;
        this.external_resource_url = external_resource_url;
        this.creq = creq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getExternalResourceUrl() {
        return external_resource_url;
    }

    public void setExternalResourceUrl(String external_resource_url) {
        this.external_resource_url = external_resource_url;
    }

    public String getCreq() {
        return creq;
    }

    public void setCreq(String creq) {
        this.creq = creq;
    }
}
