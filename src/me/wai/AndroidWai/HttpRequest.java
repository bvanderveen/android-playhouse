package me.wai.AndroidWai;

import java.nio.ByteBuffer;
import java.util.Map;

public class HttpRequest {
    String method, path;
    Map<String, String> headers;
    ByteBuffer body;

    public static HttpRequest create(String method, String path, Map<String, String> headers, ByteBuffer body) {
        HttpRequest result = new HttpRequest();
        result.method = method;
        result.path = path;
        result.body=body;
        result.headers = headers;
        return result;
    }
}
