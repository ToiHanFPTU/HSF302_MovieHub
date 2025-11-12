package com.g_wuy.hsf302.moviehub.model.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VNPayRequest {
    private Long amount;
    private String orderInfo;
    private String orderType;
    private String bankCode;
    private String language;
}