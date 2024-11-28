package com.example.happy_dream_app;

import com.example.happy_dream_app.Service.ChargerService;
import com.example.happy_dream_app.Util.LocalDateAdapter;
import com.example.happy_dream_app.Util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // 로컬 개발 서버 (끝에 슬래시 추가)
    private static final String NAVER_LOCAL_SEARCH_BASE_URL = "https://openapi.naver.com/v1/search/"; // 네이버 지역 검색 API Base URL (끝에 슬래시 추가)
    private static final String NAVER_COORDINATE_CONVERT_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"; // 네이버 좌표 변환 API Base URL (끝에 슬래시 추가)

    private static Retrofit retrofit;
    private static Retrofit naverLocalSearchRetrofit = null;
    private static Retrofit naverCoordinateConvertRetrofit = null;

    private static Retrofit getRetrofitInstance(String baseUrl) {
        // 로깅 인터셉터 설정
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // 요청과 응답의 모든 내용을 로그에 남김

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client) // OkHttp 클라이언트 추가
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .serializeNulls()  // null 값도 직렬화/역직렬화 허용
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    // 네이버 지역 검색 API 클라이언트
    public static Retrofit getNaverLocalSearchRetrofit() {
        if (naverLocalSearchRetrofit == null) {
            naverLocalSearchRetrofit = getRetrofitInstance(NAVER_LOCAL_SEARCH_BASE_URL);
        }
        return naverLocalSearchRetrofit;
    }

    // 네이버 좌표 변환 API 클라이언트 (현재 사용하지 않음)
    // public static Retrofit getNaverCoordinateConvertRetrofit() {
    //     if (naverCoordinateConvertRetrofit == null) {
    //         naverCoordinateConvertRetrofit = getRetrofitInstance(NAVER_COORDINATE_CONVERT_BASE_URL);
    //     }
    //     return naverCoordinateConvertRetrofit;
    // }

    public static ChargerService getChargerService() {
        return getRetrofit().create(ChargerService.class);
    }
}
