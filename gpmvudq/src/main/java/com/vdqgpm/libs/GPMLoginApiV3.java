package com.vdqgpm.libs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vdqgpm.models.Profile;
import com.vdqgpm.models.WebDriverConfig;
import com.vdqgpm.utilities.Utils;

import javafx.scene.control.Alert.AlertType;

public class GPMLoginApiV3 {
	private final String COMMON_ENPOINT = "/api/v3/";
	private final String START_ENDPOINT = COMMON_ENPOINT + "profiles/start/{id}";
	private final String STOP_ENDPOINT = COMMON_ENPOINT + "profiles/close/{id}";
	private final String PROFILES_ENDPOINT = COMMON_ENPOINT + "profiles?per_page=90";

	private String apiUrl;

	public GPMLoginApiV3(String apiurl) {
		this.apiUrl = apiurl;
	}

	public WebDriverConfig startProfile(String profileId) {
		String apiUrl = (this.apiUrl + START_ENDPOINT).replace("{id}", profileId);
		String startProfile = httpGetInfoGPM(apiUrl);
		if (startProfile != null && !startProfile.isEmpty()) {
			JsonObject jsonObject = JsonParser.parseString(startProfile).getAsJsonObject();
			boolean success = jsonObject.get("success").getAsBoolean();
			if (success) {
				JsonObject dataObject = jsonObject.getAsJsonObject("data");
				String remoteAddress = dataObject.get("remote_debugging_address").getAsString();
				String driverPath = dataObject.get("driver_path").getAsString();
				return new WebDriverConfig(driverPath, remoteAddress);
			}
		}
		return null;
	}

	public String stopProfile(String profileId) {
		String apiUrl = (this.apiUrl + STOP_ENDPOINT).replace("{id}", profileId);
		System.out.println(apiUrl);
		String stopProfile = httpGetInfoGPM(apiUrl);
		if (stopProfile != null & stopProfile != "") {
			return stopProfile;
		}

		return null;
	}

	public List<Profile> fetchProfiles() {
		List<Profile> profileList = new ArrayList<>();
		String apiUrl = this.apiUrl + PROFILES_ENDPOINT;
		System.out.println(apiUrl);
		String info = httpGetInfoGPM(apiUrl);
		if (info == "" | info == null) {
			return null;
		} else {
			JsonObject jsonObject = JsonParser.parseString(info).getAsJsonObject();
			boolean success = jsonObject.get("success").getAsBoolean();
			if (success) {
				Profile profile = null;
				JsonArray dataArray = jsonObject.getAsJsonArray("data");
				for (int i = 0; i < dataArray.size(); i++) {
					JsonObject dataObject = dataArray.get(i).getAsJsonObject();
					String id = dataObject.get("id").getAsString();
					String name = dataObject.get("name").getAsString();
					String rawProxy = dataObject.get("raw_proxy").getAsString();
					String browserType = dataObject.get("browser_type").getAsString();
					int groupId = dataObject.get("group_id").getAsInt();
					String createdAt = dataObject.get("created_at").getAsString();
					profile = new Profile(id, name, createdAt, rawProxy, browserType);
					profileList.add(profile);
				}
				return profileList;
			}

		}
		return null;
	}

	// Class level variable
	private static final CloseableHttpClient httpClient = HttpClients.createDefault();

	private static String httpGetInfoGPM(String apiUrl) {
		try {
			HttpGet request = new HttpGet(apiUrl);
			request.addHeader("User-Agent", "Mozilla/5.0");
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				int statusCode = response.getCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					Utils.showAlert("Thông báo", "Lỗi Server", AlertType.WARNING);
				}
			} catch (ParseException e) {
				Utils.showAlert("Lỗi", "Kiểm tra lại kết nối của bạn: ", AlertType.ERROR);
				e.printStackTrace();
			}
		} catch (IOException e) {
			Utils.showAlert("Lỗi", "Kiểm tra lại kết nối của bạn: " + e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
		return null;
	}

	private static String httpGetInfoGPM2(String apiUrl) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(apiUrl);
			request.addHeader("User-Agent", "Mozilla/5.0");
			try (@SuppressWarnings("deprecation")
			CloseableHttpResponse response = httpClient.execute(request)) {
				int statusCode = response.getCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					Utils.showAlert("Thông báo", "Phản hồi không thành công: Mã trạng thái = " + statusCode,
							AlertType.WARNING);
				}
			} catch (ParseException e) {
				Utils.showAlert("Lỗi", "Kiểm tra lại kết nối của bạn: ", AlertType.ERROR);
				e.printStackTrace();
			}
		} catch (IOException e) {
			Utils.showAlert("Lỗi", "Kiểm tra lại kết nối của bạn: " + e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
		return null;
	}

}
