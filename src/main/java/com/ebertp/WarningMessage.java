package com.ebertp;


public final class WarningMessage {

	private String message;
	private long d;
	
	public WarningMessage(String message){
		this.message=message;
		d= System.currentTimeMillis();
	}
	
	public String getMessage(){
		return message;
	}
	
	public boolean isOutDated(){
		long now = System.currentTimeMillis();
		return (now-d)<6000;
	}
	
}
