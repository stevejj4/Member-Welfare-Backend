package com.SUNData.MemberApp.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.client.RestClient;

import java.util.List;

public class RegisterMemberRequestDTO {

    @NotNull
    @Valid
    private PrincipalMemberDTO principal;

    @NotNull
    @Valid
    private NextOfKinDTO nextOfKin;

    @Valid
    private List<DependantDTO> dependants;

    public @NotNull @Valid PrincipalMemberDTO getPrincipal() {
        return principal;
    }

    public void setPrincipal(@NotNull @Valid PrincipalMemberDTO principal) {
        this.principal = principal;
    }

    public @NotNull @Valid NextOfKinDTO getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(@NotNull @Valid NextOfKinDTO nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public @Valid List<DependantDTO> getDependants() {
        return dependants;
    }

    public void setDependants(@Valid List<DependantDTO> dependants) {
        this.dependants = dependants;
    }

}