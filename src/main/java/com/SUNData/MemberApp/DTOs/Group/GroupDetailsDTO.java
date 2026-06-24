package com.SUNData.MemberApp.DTOs.Group;

import com.SUNData.MemberApp.DTOs.Member.PrincipalMemberDTO;

import java.util.List;

public class GroupDetailsDTO {
    private MemberGroupDTO group;
    private int totalMembers;
    private List<PrincipalMemberDTO> members;

    public GroupDetailsDTO() {}

    public GroupDetailsDTO(MemberGroupDTO group, List<PrincipalMemberDTO> members) {
        this.group = group;
        this.members = members;
        this.totalMembers = members == null ? 0 : members.size();
    }

    public MemberGroupDTO getGroup() {
        return group;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public List<PrincipalMemberDTO> getMembers() {
        return members;
    }
}
