package com.fantasy.policy_search_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestModel {
    private int id;
    private String title;
    private String htmlTitle;
}
