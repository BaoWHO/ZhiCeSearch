package com.fantasy.policy_search_server.utils;

import java.util.ArrayList;
import java.util.List;

public class EmailUtil {
    private static final List<String> emailContent = new ArrayList<>();
    public static void save(String log) {
        emailContent.add(log);
    }
    public static List<String> getContent() {
        return emailContent;
    }
    public static void clear() {
        emailContent.clear();
    }
}
