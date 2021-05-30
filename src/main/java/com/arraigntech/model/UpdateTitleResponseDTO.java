package com.arraigntech.model;

public class UpdateTitleResponseDTO {

	private UpdateTitleDTO updateTitleDTO;
	private boolean flag;

	public UpdateTitleDTO getUpdateTitleDTO() {
		return updateTitleDTO;
	}

	public void setUpdateTitleDTO(UpdateTitleDTO updateTitleDTO) {
		this.updateTitleDTO = updateTitleDTO;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public UpdateTitleResponseDTO(UpdateTitleDTO updateTitleDTO, boolean flag) {
		super();
		this.updateTitleDTO = updateTitleDTO;
		this.flag = flag;
	}

	public UpdateTitleResponseDTO() {
		
	}
}
