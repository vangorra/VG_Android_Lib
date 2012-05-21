package com.vg.lib.module;

public class ModuleRuntimeException extends RuntimeException {

	public ModuleRuntimeException() {
	}

	public ModuleRuntimeException(String detailMessage) {
		super(detailMessage);
	}

	public ModuleRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public ModuleRuntimeException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
