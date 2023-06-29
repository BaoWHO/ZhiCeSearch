package com.fantasy.policy_search_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoPointModel {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
