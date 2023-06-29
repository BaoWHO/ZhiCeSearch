package com.fantasy.policy_search_server.model;

public class SearchHistoryModel {
    private Integer id;

    private Long userId;

    private String keyword;

    public SearchHistoryModel(Integer id, Long userId, String keyword) {
        this.id = id;
        this.userId = userId;
        this.keyword = keyword;
    }

    public SearchHistoryModel() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }
}