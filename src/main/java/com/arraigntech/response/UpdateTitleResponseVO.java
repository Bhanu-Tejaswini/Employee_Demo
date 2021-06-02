package com.arraigntech.response;

public class UpdateTitleResponseVO {

	private UpdateTitleVO updateTitleDTO;
	private boolean flag;

	public UpdateTitleVO getUpdateTitleDTO() {
		return updateTitleDTO;
	}

	public void setUpdateTitleDTO(UpdateTitleVO updateTitleDTO) {
		this.updateTitleDTO = updateTitleDTO;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public UpdateTitleResponseVO(UpdateTitleVO updateTitleDTO, boolean flag) {
		super();
		this.updateTitleDTO = updateTitleDTO;
		this.flag = flag;
	}

	public UpdateTitleResponseVO() {
		
	}

	@Override
	public String toString() {
		return "UpdateTitleResponseDTO [updateTitleDTO=" + updateTitleDTO + ", flag=" + flag + "]";
	}
}
