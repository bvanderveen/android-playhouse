package me.wai.AndroidWai;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import rx.Observable;
import rx.util.functions.Func1;

import java.util.ArrayList;

public class ClientImpl implements Client {

    HttpClient client;

    public ClientImpl(HttpClient client) {
        this.client = client;
    }

    static <T> Observable<ArrayList<T>> mapJsonArrayResponseBody(Observable<HttpResponse> source, final Class<T> tClass) {
        return source.map(new Func1<HttpResponse, ArrayList<T>>() {
            @Override
            public ArrayList<T> call(HttpResponse httpResponse) {
                try {
                    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
                    ArrayList<T> results = mapper.readValue(httpResponse.body, TypeFactory.collectionType(ArrayList.class, tClass));
                    return results;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public Observable<ArrayList<NewsItem>> getHackerNews() {
        return mapJsonArrayResponseBody(
                client.performRequest(HttpRequest.create("GET", "/api/hacker_news.json", null, null)),
                NewsItem.class);
    }

    @Override
    public Observable<Client> login(String email, String password) {
        return null;
    }

    @Override
    public Observable<Client> createUser(User user) {
        return null;
    }

    @Override
    public Observable<Void> updateUser(User user) {
        return null;
    }

    @Override
    public Observable<User> getUser() {
        return null;
    }

    @Override
    public Observable<SessionInfo> getSessionInfo() {
        return null;
    }
}
