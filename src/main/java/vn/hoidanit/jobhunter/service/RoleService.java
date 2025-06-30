package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO.Meta;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public ResultPaginationDTO handleGetAllRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageRole.getTotalPages());
        meta.setTotal(pageRole.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageRole.getContent());
        return rs;
    }

    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role fetchById(long id) {
         Optional<Role> role = this.roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        }
        return null;
    }

    public Role createRole(Role role) {
        //check permission exist
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream().map(permission -> permission.getId()).collect(Collectors.toList());
        
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(role);
    }

    public Role updateRole(Role role) {
    Role roleDB = this.fetchById(role.getId());
    
    //check permission exist
    if (role.getPermissions() != null) {
        List<Long> reqPermissions = role.getPermissions().stream().map(permission -> permission.getId()).collect(Collectors.toList());
    
        List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
        roleDB.setPermissions(dbPermissions); // ✅ Sử dụng roleDB thay vì role
    }
    
    roleDB.setName(role.getName());
    roleDB.setDescription(role.getDescription());
    roleDB.setActive(role.isActive());
    
    // ✅ Lưu vào database
    return roleRepository.save(roleDB); // hoặc roleRepository.update(roleDB);
}

    public void deleteRole(long id) {
        this.roleRepository.deleteById(id);
    }

}
