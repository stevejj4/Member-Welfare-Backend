package com.SUNData.MemberApp.Security;

/**
 * Application permissions used for method-level PBAC (@PreAuthorize hasAuthority).
 */
public final class Permission {

    public static final String MEMBER_CREATE = "MEMBER_CREATE";
    public static final String MEMBER_READ = "MEMBER_READ";
    public static final String MEMBER_WRITE = "MEMBER_WRITE";

    private Permission() {
    }
}
