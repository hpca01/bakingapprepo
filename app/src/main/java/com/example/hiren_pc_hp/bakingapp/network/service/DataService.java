package com.example.hiren_pc_hp.bakingapp.network.service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataService {
    public final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/";
    public static Retrofit retrofit = null;

    public static Service getService(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(Service.class);
    }

}
