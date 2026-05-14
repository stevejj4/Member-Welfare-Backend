/*
package com.SUNData.MemberApp;

import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Repository.PrincipalMemberRepository;
import com.SUNData.MemberApp.Service.PrincipalMemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrincipalMemberServiceTest {

    @Mock
    private PrincipalMemberRepository repo;

    @InjectMocks
    private PrincipalMemberService service;

    @Test
    void shouldThrowWhenNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getFullMemberDetails(1L));
    }
}
*/