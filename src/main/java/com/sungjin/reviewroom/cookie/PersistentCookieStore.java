package com.sungjin.reviewroom.cookie;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

public class PersistentCookieStore implements CookieStore, Runnable {
    //Runnable 을 구현한 이유는, JVM이 셧다운 될 때 작동하는 셧다운 훅(shutdown hook)을 추가하기 위해서이다. 생성자에서 확인 가능
    CookieStore store;

    public PersistentCookieStore() {
        // CookieStore로 쿠키들을 역직렬화 한다.
        store = new CookieManager().getCookieStore();
        // JVM이 셧다운 될 때 쿠키를 메모리에 저장한다.
        Runtime.getRuntime()
            .addShutdownHook(new Thread(this));
    }
    // Runnable 구현 메서드
    // serialize cookies to persistent storage
    @Override
    public void run() {
    }
    // CookieStore 구현 메서드 
    @Override
    public void add(URI uri, HttpCookie cookie) {
        store.add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return null;
    }

    @Override
    public List<HttpCookie> getCookies() {
        return null;
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }

    
    
}
