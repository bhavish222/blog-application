package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
