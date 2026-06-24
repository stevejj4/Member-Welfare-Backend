package com.SUNData.MemberApp.Service.member;

import com.SUNData.MemberApp.DTOs.Member.MemberExistsResponseDTO;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Repository.DependantRepository;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceExistsTest {

    @Mock
    private PrincipalMemberRepository principalRepo;

    @Mock
    private DependantRepository dependantRepo;

    @Mock
    private SystemUserRepository userRepo;

    @InjectMocks
    private MemberService memberService;

    @Test
    void memberExists_ReturnsTrueForExistingNationalId() {
        when(principalRepo.existsByNationalID("30414243")).thenReturn(true);

        MemberExistsResponseDTO response = memberService.memberExists(" 30414243 ", null);

        assertTrue(response.isNationalIdExists());
        assertFalse(response.isPhoneNumberExists());
    }

    @Test
    void memberExists_ReturnsTrueForExistingPhoneNumber() {
        when(principalRepo.existsByPhoneNumber("0712345678")).thenReturn(true);

        MemberExistsResponseDTO response = memberService.memberExists(null, " 0712345678 ");

        assertFalse(response.isNationalIdExists());
        assertTrue(response.isPhoneNumberExists());
    }

    @Test
    void memberExists_ReturnsFalseForUnknownValues() {
        when(principalRepo.existsByNationalID("99999999")).thenReturn(false);
        when(principalRepo.existsByPhoneNumber("0799999999")).thenReturn(false);

        MemberExistsResponseDTO response = memberService.memberExists("99999999", "0799999999");

        assertFalse(response.isNationalIdExists());
        assertFalse(response.isPhoneNumberExists());
    }

    @Test
    void memberExists_ThrowsWhenBothParamsAreMissingOrBlank() {
        assertThrows(ValidationException.class, () -> memberService.memberExists(" ", null));
        assertThrows(ValidationException.class, () -> memberService.memberExists(null, ""));
    }
}
