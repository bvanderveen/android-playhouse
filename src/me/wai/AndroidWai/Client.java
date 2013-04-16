package me.wai.AndroidWai;

import rx.Observable;

import java.util.ArrayList;

public interface Client {
    Observable<ArrayList<NewsItem>> getHackerNews();
    Observable<Client> login(String email, String password);
    Observable<Client> createUser(User user);
    Observable<Void> updateUser(User user);
    Observable<User> getUser();
    Observable<SessionInfo> getSessionInfo();
}
