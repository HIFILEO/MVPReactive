/*
Copyright 2017 LEO LLC

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.example.mvpreactive.dagger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.example.mvpreactive.gateway.ServiceGateway;
import com.example.mvpreactive.service.ServiceApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Dagger2 {@link Module} providing application-level dependency bindings for test.
 */
@Module
public class TestApplicationModule {
    private ApplicationModule applicationModule;

    public TestApplicationModule(ApplicationModule applicationModule) {
        this.applicationModule = applicationModule;
    }

    @Provides
    @Singleton
    Context context() {
        return applicationModule.context();
    }

    /**
     * Getter for the Application class.
     *
     * @return the Application
     */
    @Provides
    @Singleton
    public Application providesApplication() {
        return applicationModule.providesApplication();
    }

    @Provides
    @Singleton
    public Resources providesResources() {
        return applicationModule.providesResources();
    }

    @Singleton
    @Provides
    public Gson providesGson(GsonBuilder builder) {
        return applicationModule.providesGson(builder);
    }

    @Provides
    public GsonBuilder providesGsonBuilder() {
        return applicationModule.providesGsonBuilder();
    }

    @Singleton
    @Provides
    public OkHttpClient.Builder providesOkHttpBuilder() {
        return applicationModule.providesOkHttpBuilder();
    }

    @Singleton
    @Provides
    public OkHttpClient providesOkHttpClient(OkHttpClient.Builder builder, HttpLoggingInterceptor.Level level) {
        return applicationModule.providesOkHttpClient(builder, level);
    }

    @Singleton
    @Provides
    public Retrofit.Builder providesRetrofitBuilder(OkHttpClient client, Gson gson) {
        return applicationModule.providesRetrofitBuilder(client, gson);
    }

    @Singleton
    @Provides
    public HttpLoggingInterceptor.Level providesHttpLoggingInterceptorLevel() {
        return applicationModule.providesHttpLoggingInterceptorLevel();
    }

    @Provides
    @Singleton
    public ServiceGateway providesGatewayInfo(ServiceApi serviceApi) {
        return applicationModule.providesGatewayInfo(serviceApi);
    }

    @Provides
    @Singleton
    public Handler providesHandler() {
        return applicationModule.providesHandler();
    }

    @Provides
    @Singleton
    public ServiceApi providesInfoServiceApi(Retrofit.Builder retrofit) {
        //Note - mock the web calls. You test the in contract testing.
        return Mockito.mock(ServiceApi.class);
    }

    @Provides
    @Singleton
    public InjectionProcessor providesComponentProvider() {
        return Mockito.spy(new InjectionProcessor() {
            @Override
            public void processInjection(Activity activity) {
                //Do nothing, unless overridden by individual test.
            }
        });
    }
}
