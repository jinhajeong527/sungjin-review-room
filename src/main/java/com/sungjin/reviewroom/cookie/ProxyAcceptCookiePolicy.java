package com.sungjin.reviewroom.cookie;

import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProxyAcceptCookiePolicy implements CookiePolicy  {
    
    String acceptedProxy;

    @Override
    public boolean shouldAccept(URI uri, HttpCookie cookie) {
        String host;
        try {
            host = InetAddress.getByName(uri.getHost()).getCanonicalHostName();
        } catch (UnknownHostException e) {
            host = uri.getHost();
        }
        
        if (HttpCookie.domainMatches(acceptedProxy, host)) {
            return true;
        }

        return CookiePolicy.ACCEPT_ORIGINAL_SERVER.shouldAccept(uri, cookie);
    }
    
}
