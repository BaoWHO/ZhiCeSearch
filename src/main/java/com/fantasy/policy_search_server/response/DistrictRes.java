package com.fantasy.policy_search_server.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictRes {
    private List<ValueLabel> province;
    private List<ValueLabel> city;
    private List<ValueLabel> county;
}
