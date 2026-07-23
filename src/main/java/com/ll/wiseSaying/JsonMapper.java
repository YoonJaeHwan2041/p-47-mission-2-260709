package com.ll.wiseSaying;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonMapper {
    public String toJson(Quote quote){
        //객체를 json모양으로 자름 복잡해보이지만 그양 json처럼 생긴거 만드는거임
        return "{\n" +
            "  \"id\": " + quote.getId() + ",\n" +
            "  \"content\": \"" + escape(quote.getContent()) + "\",\n" +
            "  \"author\": \"" + escape(quote.getAuthor()) + "\"\n" +
            "}";
    }

    //json file을 다시 quote 객체로 불러오는 과정
    public Quote fromJson(String json){
        int id = Integer.parseInt(extract(json, "id", false));
        String content = unescape(extract(json, "content", true));
        String author = unescape(extract(json, "author", true));
        return new Quote(id, content, author, LocalDateTime.now(), LocalDateTime.now());
    }

    //json값의 형태를 정규식으로 찾아서 값만 뽑아내는 헬퍼
    // 솔직히 정규식 읽는거 포기해버렸습니다.
    private String extract(String json, String key, boolean isString) {
        String pattern = isString
            ? "\"" + key + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\""
            : "\"" + key + "\"\\s*:\\s*(-?\\d+)";
        Matcher m = Pattern.compile(pattern).matcher(json);
        if (m.find()) return m.group(1);
        throw new IllegalStateException("JSON 파싱 실패: " + key);
    }

    //문자열안에 특수문자를 json에 안전하게 넣을수 있는 형태
    private String escape(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    private String unescape(String value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                char next = value.charAt(++i);
                switch (next) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    default -> sb.append(next);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
