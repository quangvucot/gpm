package com.vdqgpm.services;

import com.vdqgpm.models.Profile;

public interface ProfileUpdateListener {
	void onProfileUpdated(Profile profile, String status);

	void onProfileError(Profile profile, String error);
}
