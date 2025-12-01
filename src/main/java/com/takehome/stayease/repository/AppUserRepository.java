package com.takehome.stayease.repository;

import java.util.Optional;
import com.takehome.stayease.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existByEmail();

    Optional<AppUser> findByEmail(String email);
}
