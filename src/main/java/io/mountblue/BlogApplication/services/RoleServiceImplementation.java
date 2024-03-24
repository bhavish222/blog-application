package io.mountblue.BlogApplication.services;

import io.mountblue.BlogApplication.entity.Role;
import io.mountblue.BlogApplication.repository.RoleRepository;

public class RoleServiceImplementation implements RoleService{
    private RoleRepository roleRepository;

    public RoleServiceImplementation(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
