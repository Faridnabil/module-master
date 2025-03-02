package com.genz.master.modules.customers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.genz.master.modules.customers.dto.CustRequestDto;
import com.genz.master.modules.users.UserEntity;
import com.genz.master.modules.users.UserRepository;
import com.genz.master.utility.PasswordHasher;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class CustService {

    private final CustRepository custRepository;

    private final UserRepository userRepository;

    private final JsonWebToken jwt;

    public List<CustEntity> getAllCustomers() {
        if (!jwt.getGroups().contains("Admin")) {
            return custRepository.find("name", jwt.getName()).list();
        }
        return custRepository.listAll();
    }

    @Transactional
    public CustEntity create(CustRequestDto request, boolean hasUserAccount) {
        CustEntity customer = new CustEntity();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail()); // Email diisi dari request
        customer.setNoTelephone(request.getNoTelephone());
        customer.setAddress(request.getAddress());

        if (hasUserAccount && request.getUser() != null) {
            // Buat user baru
            UserEntity user = new UserEntity();
            user.setUsername(request.getUser().getUsername());
            user.setEmail(request.getEmail()); // Gunakan email dari Customer

            // Hash password sebelum disimpan
            String hashedPassword = PasswordHasher.hash(request.getUser().getPassword());
            user.setPassword(hashedPassword);

            user.setRole("Customer");
            user.setStatusAktif(true);

            // Simpan user ke database
            userRepository.persist(user);

            // Hubungkan customer ke user
            customer.setUser(user);
        }

        // Simpan customer ke database
        custRepository.persist(customer);
        return customer;
    }

    public Optional<CustEntity> getCustByName(String name) {
        return custRepository.findByName(name);
    }

    @Transactional
    public Optional<CustEntity> update(Long id, CustRequestDto custDto) {
        Optional<CustEntity> existingCustOptional = custRepository.findByIdOptional(id);
        if (existingCustOptional.isPresent()) {
            CustEntity existingCust = existingCustOptional.get();

            // Perbarui data customer
            existingCust.setName(custDto.getName());
            existingCust.setEmail(custDto.getEmail()); // Perbarui email di Customer
            existingCust.setNoTelephone(custDto.getNoTelephone());
            existingCust.setAddress(custDto.getAddress());

            // Jika customer memiliki user (akun login), perbarui juga data user
            if (existingCust.getUser() != null && custDto.getUser() != null) {
                UserEntity user = existingCust.getUser();

                // Perbarui username jika diubah
                if (custDto.getUser().getUsername() != null) {
                    user.setUsername(custDto.getUser().getUsername());
                }

                // Perbarui password jika diubah
                if (custDto.getUser().getPassword() != null
                        && !custDto.getUser().getPassword().isEmpty()) {
                    String hashedPassword = PasswordHasher.hash(custDto.getUser().getPassword());
                    user.setPassword(hashedPassword);
                }

                // Perbarui email di UserEntity jika email di Customer diubah
                user.setEmail(custDto.getEmail());

                // Simpan perubahan user ke database
                userRepository.persist(user);
            }

            // Simpan perubahan customer ke database
            custRepository.persist(existingCust);
            return Optional.of(existingCust);
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<CustEntity> customerOptional = custRepository.findByIdOptional(id);
        if (customerOptional.isPresent()) {
            CustEntity customer = customerOptional.get();

            // Soft delete customer
            customer.setDeleted(true);
            custRepository.persist(customer);

            // Soft delete user terkait (jika ada)
            if (customer.getUser() != null) {
                UserEntity user = customer.getUser();
                user.setStatusAktif(false);
                ;
                userRepository.persist(user);
            }

            return true;
        }
        return false;
    }
}