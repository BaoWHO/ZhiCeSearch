package com.fantasy.policy_search_server.utils;

import com.fantasy.policy_search_server.model.UserModel;

import java.util.HashMap;
import java.util.UUID;

public class TokenUtil {
    private static final HashMap<String, Object> tokenMap = new HashMap<>();

    public static String generate(Object data) {
        UserModel userModel = (UserModel) data;
        return UUID.nameUUIDFromBytes((userModel.getId() + "::" + userModel.getPhone()).getBytes()).toString();
    }

    public static void save(String token, Object data) {
        tokenMap.put(token, data);
    }

    public static boolean contain(String token) {
        return tokenMap.containsKey(token);
    }

    public static Object getData(String token) {
        return tokenMap.get(token);
    }

    public static void invalidate(String token) {
        tokenMap.remove(token);
    }
}
