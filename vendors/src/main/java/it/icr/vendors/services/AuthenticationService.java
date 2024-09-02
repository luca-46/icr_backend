package it.icr.vendors.services;

import io.vavr.control.Either;
import io.vavr.control.Try;
import it.icr.vendors.dto.VendorRegistrationDto;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.requests.LoginRequest;
import it.icr.vendors.requests.auth.ResetPasswordRequest;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface AuthenticationService {

    public Try<Vendor> authenticate(LoginRequest request);

    public Either<Error, Void> resetPassword (ResetPasswordRequest request);

    public Either<Error, VendorRegistrationDto> register(String email);
}
