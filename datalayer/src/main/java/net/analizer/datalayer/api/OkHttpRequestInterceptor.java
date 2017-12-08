package net.analizer.datalayer.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public interface OkHttpRequestInterceptor {
    /**
     * For those who implement this interface must handle the request processing themselves and
     * return a response back.
     *
     * @param chain an interceptor chain
     * @return  a response
     * @throws IOException
     */
    Response onRequestIntercepted(Interceptor.Chain chain) throws IOException;
}
