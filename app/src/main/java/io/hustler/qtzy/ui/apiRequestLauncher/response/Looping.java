package io.hustler.qtzy.ui.apiRequestLauncher.response;

import com.google.gson.annotations.SerializedName;

//@Generated("com.robohorse.robopojogenerator")
public class Looping{

	@SerializedName("mp4")
	private String mp4;

	@SerializedName("mp4_size")
	private String mp4Size;

	public void setMp4(String mp4){
		this.mp4 = mp4;
	}

	public String getMp4(){
		return mp4;
	}

	public void setMp4Size(String mp4Size){
		this.mp4Size = mp4Size;
	}

	public String getMp4Size(){
		return mp4Size;
	}
}