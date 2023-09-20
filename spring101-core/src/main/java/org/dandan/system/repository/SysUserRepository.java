package org.dandan.system.repository;

import org.dandan.system.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    Optional<SysUser> findByUsername(String username);

    Optional<SysUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}