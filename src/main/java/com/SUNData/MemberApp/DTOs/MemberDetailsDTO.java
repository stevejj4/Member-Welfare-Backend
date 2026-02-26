package com.SUNData.MemberApp.DTOs;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Model.PrincipalMemberModel;

import java.util.List;

public class MemberDetailsDTO {
    private PrincipalMemberDTO member;
    private List<NextOfKinDTO> nextOfKin;

    public MemberDetailsDTO(PrincipalMemberDTO member, List<NextOfKinDTO> nextOfKin) {
        this.member = member;
        this.nextOfKin = nextOfKin;
    }

    // Getters are required for JSON Serialization

    public PrincipalMemberDTO getMember() {
        return member;
    }

    public List<NextOfKinDTO> getNextOfKin() {
        return nextOfKin;
    }
}