package com.SUNData.MemberApp.DTOs.Member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


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