package com.vdqgpm.services;

import com.vdqgpm.models.Profile;

import javafx.concurrent.Task;

public class ProfileTask extends Task<Void> {
	private Profile profile;
	private ProfileHandler handler;
	private int type = 0;
	private volatile boolean running = true;

	@Override
	protected Void call() throws Exception {
		if (isCancelled()) {
			updateMessage("Profile " + profile.getName() + " bị hủy");
			System.out.println("Profile " + profile.getName() + " bị hủy");
			return null;
		}

		updateMessage("Bắt đầu xử lý profile " + profile.getName());
		System.out.println("Bắt đầu xử lý profile " + profile.getName());
		handler.handleProfile(profile, type);
		updateMessage("Hoàn thành xử lý profile " + profile.getName());
		System.out.println("Hoàn thành xử lý profile " + profile.getName());

		return null;
	}

	public ProfileTask(Profile profile, ProfileHandler handler, int type) {
		this.profile = profile;
		this.handler = handler;
		this.type = type;
	}

}
