package com.fantasy.policy_search_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencySuggestModel {
    private int id;
    private String agency_full_name;
    private String html_agency_full_name;
}
