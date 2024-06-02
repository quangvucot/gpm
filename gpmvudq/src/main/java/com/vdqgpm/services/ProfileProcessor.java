package com.vdqgpm.services;

import java.awt.Toolkit;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.openqa.selenium.Dimension;

import com.vdqgpm.libs.GPMLoginApiV3;
import com.vdqgpm.models.ApiResponse;
import com.vdqgpm.models.Profile;
import com.vdqgpm.models.WebDriverConfig;
import com.vdqgpm.pages.NurturePage;

import javafx.application.Platform;

public class ProfileProcessor implements ProfileHandler {
	private GPMLoginApiV3 info;
	private Map<String, String> profileStatus;
	private String width;
	private String height;
	private ProfileUpdateListener listener;
	private List<Profile> profiles;
	private Boolean isSendEmail;

	public ProfileProcessor(String width, String height, GPMLoginApiV3 info, Map<String, String> profileStatus,
			ProfileUpdateListener listener, List<Profile> profiles, Boolean isSendEmail) {
		this.height = height;
		this.width = width;
		this.info = info;
		this.profileStatus = profileStatus;
		this.listener = listener;
		this.profiles = profiles;
		this.isSendEmail = isSendEmail;
	}

	@Override
	public void handleProfile(Profile profile, int type) {
		try {
			processProfile(profile, info, profileStatus, type);
			listener.onProfileUpdated(profile, "Profile processed successfully");
		} catch (Exception e) {
			listener.onProfileError(profile, e.getMessage());
		}
	}

	private void processProfile(Profile profile, GPMLoginApiV3 info, Map<String, String> profileStatus, int type) {
		try {
			profileStatus.put(profile.getName(), "Đang mở profile");
			ApiResponse<WebDriverConfig> profilesResponse = info.openProfile(profile.getId(), width + "," + height);
			if (profilesResponse != null && profilesResponse.getData() != null) {
				WebDriverConfig profiles = profilesResponse.getData();
				File driverPath = new File(profiles.getDriver_path());

				switch (type) {
				case 0: {
					java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					int screenWidth = screenSize.width;
					int screenHeight = screenSize.height;
					int profileWidth = Integer.parseInt(width);
					int profileHeight = Integer.parseInt(height);
					int columns = screenWidth / profileWidth;
					int rows = screenHeight / profileHeight;
					int profileIndex = getProfileIndex(profile); // Implement method to get the profile index
					int x = (profileIndex % columns) * profileWidth;
					int y = (profileIndex / columns) * profileHeight;

					if (y + profileHeight > screenHeight) {
						y = (profileIndex % rows) * profileHeight;
					}
					NurturePage nurturePage = new NurturePage();
					profile.setRunning(true);
					nurturePage.setUp(profiles.getRemote_debugging_address(), driverPath, width, height, x, y,
							isSendEmail);
					profileStatus.put(profile.getName(), "Hoàn thành nuôi profile");
					break;
				}
				case 1: {
					NurturePage nurturePage = new NurturePage();
					nurturePage.connectSingleProfile(profiles.getRemote_debugging_address(), driverPath);
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: ");
				}

			} else {
				profileStatus.put(profile.getName(), "Mở profile không thành công");
			}
		} catch (Exception e) {
			profileStatus.put(profile.getName(), "Lỗi khi xử lý");
		} finally {
			switch (type) {
			case 0: {
				info.stopProfile(profile.getId());
				profile.setRunning(false);
			}
			case 1: {

			}
			}

		}
	}

	private int getProfileIndex(Profile profile) {
		return profiles.indexOf(profile);
	}
}
