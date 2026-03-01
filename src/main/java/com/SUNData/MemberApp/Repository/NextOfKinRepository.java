package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.NextOfKinModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NextOfKinRepository extends JpaRepository<NextOfKinModel, Long> {
}