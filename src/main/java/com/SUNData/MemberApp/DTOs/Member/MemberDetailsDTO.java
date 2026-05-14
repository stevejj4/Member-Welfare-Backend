package com.SUNData.MemberApp.DTOs.Member;

import java.util.List;

public class MemberDetailsDTO {
    private PrincipalMemberDTO member;
    private NextOfKinDTO nextOfKin;
    private List<DependantDTO> dependants;

    public MemberDetailsDTO(PrincipalMemberDTO member, NextOfKinDTO nextOfKin, List<DependantDTO> dependants) {
        this.member = member;
        this.nextOfKin = nextOfKin;
        this.dependants = dependants;
    }

    // Getters are required for JSON Serialization

    public PrincipalMemberDTO getMember() {
        return member;
    }

    public NextOfKinDTO getNextOfKin() {
        return nextOfKin;
    }

    public List<DependantDTO> getDependants() {return dependants; }
}