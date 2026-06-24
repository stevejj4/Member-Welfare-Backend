package com.SUNData.MemberApp.DTOs.Member;

public class MemberExistsResponseDTO {
    private boolean nationalIdExists;
    private boolean phoneNumberExists;

    public MemberExistsResponseDTO() {}

    public MemberExistsResponseDTO(boolean nationalIdExists, boolean phoneNumberExists) {
        this.nationalIdExists = nationalIdExists;
        this.phoneNumberExists = phoneNumberExists;
    }

    public boolean isNationalIdExists() {
        return nationalIdExists;
    }

    public void setNationalIdExists(boolean nationalIdExists) {
        this.nationalIdExists = nationalIdExists;
    }

    public boolean isPhoneNumberExists() {
        return phoneNumberExists;
    }

    public void setPhoneNumberExists(boolean phoneNumberExists) {
        this.phoneNumberExists = phoneNumberExists;
    }
}
