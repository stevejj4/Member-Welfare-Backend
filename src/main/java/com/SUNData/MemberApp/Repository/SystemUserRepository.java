package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUserModel, Long> {
    Optional<SystemUserModel> findByEmail(String email);
    boolean existsByEmail(String email);
}
