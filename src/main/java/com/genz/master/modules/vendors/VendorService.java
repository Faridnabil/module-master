package com.genz.master.modules.vendors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.genz.master.modules.vendors.dto.VendorRequestDto;
import com.genz.master.modules.users.UserEntity;
import com.genz.master.modules.users.UserRepository;
import com.genz.master.utility.PasswordHasher;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    private final UserRepository userRepository;

    private final JsonWebToken jwt;

    public List<VendorEntity> getAllVendors() {
        if (!jwt.getGroups().contains("Admin")) {
            return vendorRepository.find("name", jwt.getName()).list();
        }
        return vendorRepository.listAll();
    }

    @Transactional
    public VendorEntity create(VendorRequestDto request, boolean hasUserAccount) {
        VendorEntity vendor = new VendorEntity();
        vendor.setName(request.getName());
        vendor.setEmail(request.getEmail());
        vendor.setNoTelephone(request.getNoTelephone());
        vendor.setAddress(request.getAddress());

        if (hasUserAccount && request.getUser() != null) {
            // Buat user baru
            UserEntity user = new UserEntity();
            user.setUsername(request.getUser().getUsername());

            // Hash password sebelum disimpan
            String hashedPassword = PasswordHasher.hash(request.getUser().getPassword());
            user.setPassword(hashedPassword);

            user.setEmail(request.getEmail()); // Gunakan email dari Vendor
            user.setRole("Vendor"); // Role khusus untuk vendor
            user.setStatusAktif(true);

            // Simpan user ke database
            userRepository.persist(user);

            // Hubungkan vendor ke user
            vendor.setUser(user);
        }

        // Simpan vendor ke database
        vendorRepository.persist(vendor);
        return vendor;
    }

    public Optional<VendorEntity> getVendorByName(String name) {
        return vendorRepository.findByName(name);
    }

    @Transactional
    public Optional<VendorEntity> update(Long id, VendorRequestDto vendorDto) {
        Optional<VendorEntity> existingVendorOptional = vendorRepository.findByIdOptional(id);
        if (existingVendorOptional.isPresent()) {
            VendorEntity existingVendor = existingVendorOptional.get();

            // Perbarui data vendor
            existingVendor.setName(vendorDto.getName());
            existingVendor.setEmail(vendorDto.getEmail());
            existingVendor.setNoTelephone(vendorDto.getNoTelephone());
            existingVendor.setAddress(vendorDto.getAddress());

            // Jika vendor memiliki user (akun login), perbarui juga data user
            if (existingVendor.getUser() != null && vendorDto.getUser() != null) {
                UserEntity user = existingVendor.getUser();

                // Perbarui username jika diubah
                if (vendorDto.getUser().getUsername() != null) {
                    user.setUsername(vendorDto.getUser().getUsername());
                }

                // Perbarui password jika diubah
                if (vendorDto.getUser().getPassword() != null
                        && !vendorDto.getUser().getPassword().isEmpty()) {
                    String hashedPassword = PasswordHasher.hash(vendorDto.getUser().getPassword());
                    user.setPassword(hashedPassword);
                }

                // Simpan perubahan user ke database
                userRepository.persist(user);
            }

            // Simpan perubahan vendor ke database
            vendorRepository.persist(existingVendor);
            return Optional.of(existingVendor);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<VendorEntity> vendorOptional = vendorRepository.findByIdOptional(id);
        if (vendorOptional.isPresent()) {
            VendorEntity vendor = vendorOptional.get();

            // Soft delete vendor
            vendor.setDeleted(true);
            vendorRepository.persist(vendor);

            // Soft delete user terkait (jika ada)
            if (vendor.getUser() != null) {
                UserEntity user = vendor.getUser();
                user.setStatusAktif(false);
                userRepository.persist(user);
            }

            return true;
        }
        return false;
    }
}