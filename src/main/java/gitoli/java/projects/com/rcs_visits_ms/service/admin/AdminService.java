package gitoli.java.projects.com.rcs_visits_ms.service.admin;

import gitoli.java.projects.com.rcs_visits_ms.dto.admin.CreateAdminDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.admin.SearchAdminDTO;
import gitoli.java.projects.com.rcs_visits_ms.dto.admin.AdminDTO;
import gitoli.java.projects.com.rcs_visits_ms.entity.admin.Admin;
import gitoli.java.projects.com.rcs_visits_ms.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AdminDTO createAdmin(CreateAdminDTO dto) {
        if (adminRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (adminRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        if (adminRepository.existsByOfficeName(dto.getOfficeName())) {
            throw new IllegalArgumentException("Office name already exists");
        }

        Admin admin = mapToEntity(dto);
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin = adminRepository.save(admin);
        return mapToDTO(admin);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
    public AdminDTO updateAdmin(UUID id, CreateAdminDTO dto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + id));

        // Check for duplicates (excluding current admin)
        adminRepository.findByEmail(dto.getEmail())
                .filter(a -> !a.getId().equals(id))
                .ifPresent(a -> { throw new IllegalArgumentException("Email already exists"); });
        adminRepository.findByPhoneNumber(dto.getPhoneNumber())
                .filter(a -> !a.getId().equals(id))
                .ifPresent(a -> { throw new IllegalArgumentException("Phone number already exists"); });
        adminRepository.findByOfficeName(dto.getOfficeName())
                .filter(a -> !a.getId().equals(id))
                .ifPresent(a -> { throw new IllegalArgumentException("Office name already exists"); });

        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        admin.setPhoneNumber(dto.getPhoneNumber());
        admin.setOfficeName(dto.getOfficeName());
        admin.setSignature(dto.getSignature());
        admin.setStamp(dto.getStamp());
        admin.setRole(dto.getRole());

        admin = adminRepository.save(admin);
        return mapToDTO(admin);
    }

    @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
    public AdminDTO getAdmin(UUID id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + id));
        return mapToDTO(admin);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AdminDTO getAdminByEmail(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with email: " + email));
        return mapToDTO(admin);
    }

    @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
    public void deleteAdmin(UUID id) {
        if (!adminRepository.existsById(id)) {
            throw new IllegalArgumentException("Admin not found with ID: " + id);
        }
        adminRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminDTO> searchAdmins(SearchAdminDTO dto, Pageable pageable) {
        if (dto.getOfficeName() != null && !dto.getOfficeName().isBlank()) {
            return adminRepository.findByOfficeName(dto.getOfficeName(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getCreatedAt() != null || dto.getUpdatedAt() != null) {
            return adminRepository.findByCreatedAtAndUpdatedAt(dto.getCreatedAt(), dto.getUpdatedAt(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getStartDate() != null || dto.getEndDate() != null) {
            return adminRepository.findByDateRange(dto.getStartDate(), dto.getEndDate(), pageable)
                    .map(this::mapToDTO);
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            return adminRepository.findByName(dto.getName(), pageable)
                    .map(this::mapToDTO);
        }
        return adminRepository.findByIsActiveTrue(pageable).map(this::mapToDTO);
    }

    private Admin mapToEntity(CreateAdminDTO dto) {
        return Admin.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .officeName(dto.getOfficeName())
                .signature(dto.getSignature())
                .stamp(dto.getStamp())
                .role(dto.getRole())
                .isActive(true)
                .build();
    }

    private AdminDTO mapToDTO(Admin admin) {
        return AdminDTO.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .email(admin.getEmail())
                .phoneNumber(admin.getPhoneNumber())
                .officeName(admin.getOfficeName())
                .role(admin.getRole())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .isActive(admin.isActive())
                .build();
    }
}