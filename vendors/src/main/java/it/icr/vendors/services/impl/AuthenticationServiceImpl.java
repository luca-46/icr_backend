package it.icr.vendors.services.impl;

import io.vavr.control.Either;
import io.vavr.control.Try;
import it.icr.vendors.dto.VendorRegistrationDto;
import it.icr.vendors.entities.State;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.repositories.StateRepository;
import it.icr.vendors.repositories.VendorRepository;
import it.icr.vendors.requests.LoginRequest;
import it.icr.vendors.requests.auth.ResetPasswordRequest;
import it.icr.vendors.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StateRepository stateRepository;

    @Override
    public Try<Vendor> authenticate(LoginRequest request) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            return Try.failure(new RuntimeException("Invalid credentials"));
        }

        Optional<Vendor> tryVendor = vendorRepository.findByEmail(request.getUsername());

        return tryVendor.map(Try::success).orElseGet(() -> Try.failure(new RuntimeException("User not found")));

    }

    BiFunction<String, String, Either<Error, Boolean>> passwordMatches =
            (password, encodedPassword) -> Either.right(passwordEncoder.matches(password, encodedPassword));


    BiFunction<Vendor, String ,Either<Error, Boolean>> setNewPassword =
            (vendor, newPassword) -> {
                return Try.of(() -> {
                            vendor.setPassword(passwordEncoder.encode(newPassword));
                            vendor.setIsFirstAccess(false);
                            vendorRepository.save(vendor);
                            return true;
                        })
                        .toEither()
                        .mapLeft(ex -> new Error("Something went wrong"));
            };


    @Override
    public Either<Error, Void> resetPassword (ResetPasswordRequest request) {
        return vendorRepository.findById(request.getId())
                .map(vendor -> {
                    return passwordMatches.apply(request.getOld_password(), vendor.getPassword())
                            .mapLeft(error -> new Error("Runtime Error"))
                            .peekLeft(error -> { throw new RuntimeException(error.getMessage()); })
                            .flatMap(match -> {
                                if (match) {
                                    return setNewPassword.apply(vendor, request.getNew_password())
                                            .map(success -> Either.<Error, Void>right(null))
                                            .getOrElseGet(Either::left);
                                } else {
                                    return Either.left(new Error("Password mismatch"));
                                }
                            });
                })
                .orElse(Either.left(new Error("User not found")));
    }

    @Override
    public Either<Error, VendorRegistrationDto> register(String email){
        Optional<Vendor> existingVendor = vendorRepository.findByEmail(email);

        if(existingVendor.isPresent()){
            return Either.left(new Error("Vendor already exists"));
        }

        String tempPassword = "1234";

        Vendor vendor = new Vendor();
        vendor.setId(UUID.randomUUID().toString().replace("-", "").substring(0,10));
        vendor.setEmail(email);
        vendor.setPassword(passwordEncoder.encode(tempPassword));
        vendor.setIsFirstAccess(true);
        vendor.setCreatedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());
        vendor.setState(new State(1, "PENDING_REGISTRATION"));
        vendor.setCompanyName(null);
        vendor.setVatNumber(null);
        vendor.setTechReferent(null);
        vendor.setAdminReferent(null);
        vendor.setPhone(null);
        vendor.setPec(null);

        try{
            Vendor savedVendor = vendorRepository.save(vendor);
            VendorRegistrationDto result = new VendorRegistrationDto(savedVendor, tempPassword);
            return Either.right(result);
        }catch(Exception e){
            return Either.left(new Error("Something went wrong"));
        }
    }

}
