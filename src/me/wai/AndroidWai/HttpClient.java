package me.wai.AndroidWai;

import rx.Observable;

public interface HttpClient {
    public Observable<HttpResponse> performRequest(HttpRequest request);
}
