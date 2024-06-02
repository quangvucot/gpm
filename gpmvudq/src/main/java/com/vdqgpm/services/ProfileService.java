package com.vdqgpm.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import com.vdqgpm.libs.ApiResponseSimple;
import com.vdqgpm.libs.GPMLoginApiV3;
import com.vdqgpm.models.ApiResponse;
import com.vdqgpm.models.Profile;
import com.vdqgpm.utilities.AlertActionHandler;
import com.vdqgpm.utilities.AlertUtils;
import javafx.application.Platform;
import javafx.scene.control.TableView;

public class ProfileService {
	private static ProfileService instance;
	GPMLoginApiV3 info;

	private ProfileService() {
		// Khởi tạo nếu cần
	}

	private ExecutorService executorService;
	private Map<Profile, Future<?>> futureMap = new ConcurrentHashMap<>();

	public static ProfileService getInstance() {
		if (instance == null) { // Kiểm tra lần đầu
			synchronized (ProfileService.class) {
				if (instance == null) { // Kiểm tra lần thứ hai, trong khối synchronized
					instance = new ProfileService();
				}
			}
		}
		return instance;
	}

	public List<Profile> getSelectedProfiles(List<Profile> profiles) {
		return profiles.stream().filter(Profile::isSelected).collect(Collectors.toList());
	}

	public void startProfile(TableView<Profile> profileTable, String url, ProfileUpdateListener listener) {
		AlertUtils.showConfirmationAlert("Bắt đầu profile ", "Chạy các profile đã chọn?", new AlertActionHandler() {

			@Override
			public void onSuccess() {
				List<Profile> selectedProfiles = getSelectedProfiles(profileTable.getItems());
				System.out.println("Số lượng profile chọn " + selectedProfiles.size());
				executorService = Executors.newFixedThreadPool(selectedProfiles.size());
				info = new GPMLoginApiV3(url);
				Map<String, String> profileStatus = new ConcurrentHashMap<>();
				ProfileProcessor processor = new ProfileProcessor("", "", info, profileStatus, listener,
						selectedProfiles, false);
				for (Profile profile : selectedProfiles) {
					profile.setRunning(true);
					ProfileTask task = new ProfileTask(profile, processor, 1);
					Future<?> future = executorService.submit(task);
					futureMap.put(profile, future);
				}
				executorService.shutdown();
			}

			@Override
			public void onFailure() {

			}
		});
	}

	public void stopProfile(String url, TableView<Profile> profileTable) {
		AlertUtils.showConfirmationAlert("Dừng Profile", "Dừng tất cả profile đã chọn?", new AlertActionHandler() {

			@Override
			public void onSuccess() {
				List<Profile> selectedProfiles = getSelectedProfiles(profileTable.getItems());
				for (Profile profile : selectedProfiles) {
					info = new GPMLoginApiV3(url);
					Future<?> future = futureMap.get(profile);
					if (future != null) {
						ApiResponseSimple stopProfile = info.stopProfile(profile.getId());
						future.cancel(true);
						System.out.println("Cancelled profile " + profile.getName());
						futureMap.remove(profile);
						Platform.runLater(() -> {
							if (stopProfile != null) {
								profile.setRunning(false);
							}
						});
					}

				}
			}

			@Override
			public void onFailure() {
				// Xử lý khi người dùng không xác nhận
			}
		});
	}

	public void nurtureProfile(String url, TableView<Profile> profileTable, Boolean isSendEm,
			ProfileUpdateListener listener, int streamNumber, String width, String height) {
		AlertUtils.showConfirmationAlert("Nuôi Profile", "Nuôi tất cả profile đã chọn?", new AlertActionHandler() {
			@Override
			public void onSuccess() {

				List<Profile> selectedProfiles = profileTable.getItems().stream().filter(Profile::isSelected)
						.collect(Collectors.toList());

				ExecutorService executorService = Executors.newFixedThreadPool(streamNumber);
				info = new GPMLoginApiV3(url);
				Map<String, String> profileStatus = new ConcurrentHashMap<>();
				ProfileProcessor processor = null;
				if (isSendEm) {
					processor = new ProfileProcessor(width, height, info, profileStatus, listener, selectedProfiles,
							isSendEm);

				} else {
					processor = new ProfileProcessor(width, height, info, profileStatus, listener, selectedProfiles,
							isSendEm);
				}

				for (Profile profile : selectedProfiles) {
					ProfileTask task = new ProfileTask(profile, processor, 0);
					executorService.submit(task);
				}

				executorService.shutdown();
			}

			@Override
			public void onFailure() {
				// Xử lý khi người dùng không xác nhận
			}
		});
	}

	public void loadProfiles(String api, TableView<Profile> profileTable) {
		ApiResponse<List<Profile>> dataResponce = fetchProfilesFromDataSource(api, profileTable);
		List<Profile> profiles = dataResponce.getData();
		if (profiles != null && profiles.size() > 0) {
			profileTable.getItems().setAll(profiles); // Xử lý bảng dữ liệu
		}
	}

	public void stopSingleProfile(String api, Profile profile) {
		info = new GPMLoginApiV3(api);
		info.stopProfile(profile.getId());
	}

	public void startSingleProfile(String api, Profile profile) {
		info = new GPMLoginApiV3(api);
		info.stopProfile(profile.getId());
	}

	private ApiResponse<List<Profile>> fetchProfilesFromDataSource(String api, TableView<Profile> profileTable) {
		info = new GPMLoginApiV3(api);
		ApiResponse<List<Profile>> profilesResponse = null;
		try {
			profilesResponse = info.fetchProfileData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return profilesResponse;
	}
}
