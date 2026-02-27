package com.SUNData.MemberApp.DTOs;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import com.SUNData.MemberApp.Model.PrincipalMemberModel;
import com.SUNData.MemberApp.Model.DependantModel;

import java.util.List;

public class MemberDetailsDTO {
    private PrincipalMemberDTO member;
    private List<NextOfKinDTO> nextOfKin;
    private List<DependantDTO> dependants;

    public MemberDetailsDTO(PrincipalMemberDTO member, List<NextOfKinDTO> nextOfKin, List<DependantDTO> dependants) {
        this.member = member;
        this.nextOfKin = nextOfKin;
        this.dependants = dependants;
    }

    // Getters are required for JSON Serialization

    public PrincipalMemberDTO getMember() {
        return member;
    }

    public List<NextOfKinDTO> getNextOfKin() {
        return nextOfKin;
    }

    public List<DependantDTO> getDependants() {return dependants; }
}