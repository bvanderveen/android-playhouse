package me.wai.AndroidWai;

import java.util.List;
import java.util.Map;

public class HttpResponse {
    int status;
    Map<String,List<String>> headers;
    String body;
}
