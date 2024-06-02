package com.vdqgpm.libs;

import java.util.List;
import com.vdqgpm.models.ApiResponse;
import com.vdqgpm.models.Profile;
import com.vdqgpm.models.WebDriverConfig;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GPMApiService {

	@GET("/api/v3/profiles?per_page=90")
	Call<ApiResponse<List<Profile>>> getAllProfileData();

	@GET("/api/v3/profiles/start/{id}")
	Call<ApiResponse<WebDriverConfig>> getOnceProfile(@Path("id") String id, @Query("win_pos") String dimention);

	@GET("/api/v3/profiles/close/{id}")
	Call<ApiResponseSimple> stopProfile(@Path("id") String id);
}
