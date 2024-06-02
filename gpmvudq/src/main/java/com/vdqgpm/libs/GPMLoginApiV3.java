package com.vdqgpm.libs;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.vdqgpm.models.ApiResponse;
import com.vdqgpm.models.Profile;
import com.vdqgpm.models.WebDriverConfig;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GPMLoginApiV3 {

	private Retrofit retrofit;
	private static GPMApiService apiService;

	public GPMLoginApiV3(String apiurl) {
		OkHttpClient okHttpClient = OkHttpClientSingleton.getInstance();
		retrofit = new Retrofit.Builder().baseUrl(apiurl).client(okHttpClient)
				.addConverterFactory(GsonConverterFactory.create()).build();
		apiService = retrofit.create(GPMApiService.class);
	}

	public ApiResponseSimple stopProfile(String profileId) {
		try {
			Call<ApiResponseSimple> call = apiService.stopProfile(profileId);
			Response<ApiResponseSimple> response = call.execute();
			if (response.isSuccessful() && response.body() != null) {
				return response.body();
			} else {
				System.err.println("Response error: " + response.message());
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public ApiResponse<List<Profile>> fetchProfileData() throws IOException {

		Call<ApiResponse<List<Profile>>> call = apiService.getAllProfileData();
		try {
			Response<ApiResponse<List<Profile>>> response = call.execute();
			if (response.isSuccessful() && response.body() != null) {
				return response.body();
			} else {
				System.err.println("Response error: " + response.message());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ApiResponse<WebDriverConfig> openProfile(String id, String dimention) {
		Call<ApiResponse<WebDriverConfig>> call = apiService.getOnceProfile(id, dimention);
		try {
			Response<ApiResponse<WebDriverConfig>> response = call.execute();
			if (response.isSuccessful() && response.body() != null) {
				return response.body();
			} else {
				System.err.println("Response error: " + response.message());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(); // In ra ngoại lệ để dễ dàng gỡ lỗi
			return null;
		}

	}
}
