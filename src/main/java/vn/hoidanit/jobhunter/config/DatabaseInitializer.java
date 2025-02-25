package vn.hoidanit.jobhunter.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(
        PermissionRepository permissionRepository,
        RoleRepository roleRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> permissions = new ArrayList<>();
            permissions.add(new Permission("Create a Company", "/api/v1/companies", "POST", "COMPANIES"));
            permissions.add(new Permission("Update a Company", "/api/v1/companies", "PUT", "COMPANIES"));
            permissions.add(new Permission("Delete a Company", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
            permissions.add(new Permission("Get a Company by Id", "/api/v1/companies/{id}", "GET", "COMPANIES"));
            permissions.add(new Permission("Get Company with Pagination", "/api/v1/companies", "GET", "COMPANIES"));

            permissions.add(new Permission("Create a Job", "/api/v1/jobs", "POST", "JOBS"));
            permissions.add(new Permission("Update a Job", "/api/v1/jobs", "PUT", "JOBS"));
            permissions.add(new Permission("Delete a Job", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
            permissions.add(new Permission("Get a Job by Id", "/api/v1/jobs/{id}", "GET", "JOBS"));
            permissions.add(new Permission("Get Job with Pagination", "/api/v1/jobs", "GET", "JOBS"));

            permissions.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            permissions.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            permissions.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            permissions.add(new Permission("Get a permission by Id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            permissions.add(new Permission("Get permission with Pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

            permissions.add(new Permission("Create a resume", "/api/v1/resumes", "POST", "RESUMES"));
            permissions.add(new Permission("Update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
            permissions.add(new Permission("Delete a resume", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
            permissions.add(new Permission("Get a resume by Id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
            permissions.add(new Permission("Get resume with Pagination", "/api/v1/resumes", "GET", "RESUMES"));

            permissions.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            permissions.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            permissions.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            permissions.add(new Permission("Get a role by Id", "/api/v1/roles/{id}", "GET", "ROLES"));
            permissions.add(new Permission("Get role with Pagination", "/api/v1/roles", "GET", "ROLES"));

            permissions.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            permissions.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            permissions.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            permissions.add(new Permission("Get a user by Id", "/api/v1/users/{id}", "GET", "USERS"));
            permissions.add(new Permission("Get user with Pagination", "/api/v1/users", "GET", "USERS"));

            permissions.add(new Permission("Create a subscriber", "/api/v1/subscribers", "POST", "SUBSCRIBERS"));
            permissions.add(new Permission("Update a subscriber", "/api/v1/subscribers", "PUT", "SUBSCRIBERS"));
            permissions.add(new Permission("Delete a subscriber", "/api/v1/subscribers/{id}", "DELETE", "SUBSCRIBERS"));
            permissions.add(new Permission("Get a subscriber by Id", "/api/v1/subscribers/{id}", "GET", "SUBSCRIBERS"));
            permissions.add(new Permission("Get subscriber with Pagination", "/api/v1/subscribers", "GET", "SUBSCRIBERS"));

            permissions.add(new Permission("Download a file", "/api/v1/files", "POST", "FILES"));
            permissions.add(new Permission("Upload a file", "/api/v1/files", "GET", "FILES"));

            this.permissionRepository.saveAll(permissions);
        }
   
        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = new Role();

            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin full Permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);
            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("TP Ho Chi Minh");
            adminUser.setAge(22);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("David Loc");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));

            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }
            this.userRepository.save(adminUser);
        }
        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println("SKIP INIT DATABASE ~ ALREADY HAVE DATA");
        } else {
            System.out.println(">>> END INIT DATABASE");
        }
    }
    
} 
