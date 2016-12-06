package com.ebertp;

public final class WarningMessage {

	private final long d;
	private final String message;

	public WarningMessage(final String message) {
		this.message = message;
		d = System.currentTimeMillis();
	}

	public String getMessage() {
		return message;
	}

	public boolean isOutDated() {
		final long now = System.currentTimeMillis();
		return now - d < 6000;
	}

}
