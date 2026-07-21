package com.ll.wiseSaying;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class QuoteRequest {
    String actionName;
    Map<String, String> paramMap = new HashMap<>();

    public QuoteRequest(String cmd){
        //2줄
        String[] cmdBits = cmd.split("\\?", 2);
        //명령어 입장
        actionName = cmdBits[0];
        String queryString = cmdBits.length > 1 ? cmdBits[1] : "";
        String[] queryStringBits = queryString.split("&");

        paramMap = Arrays.stream(queryStringBits)
                .map(part -> part.split("="))
                .filter(bits -> bits.length == 2)
                .collect(
                        Collectors.toMap(
                                bits -> bits[0].toLowerCase(),
                                bits-> bits[1].toLowerCase()
                        )
                );


    }

    public String getActionName() {
        return actionName;
    }

    public String getParam(String key, String defaultValue){
        return paramMap.getOrDefault(key, defaultValue);
    }

    public int getParamAsInt(String key, int defaultValue) {
        //목록만 작성할시 잘못된값 나오길레 값이 없으면 defaultValue로 감
        if(!paramMap.containsKey(key)){
            return defaultValue;
        }
        String rst = getParam(key, "");

        try {
            return Integer.parseInt(rst);
        } catch (NumberFormatException e){
            System.out.println("잘못된 입력값을 넣어서 기본값으로 반환됩니다.");
            return defaultValue;
        }
    }
}
