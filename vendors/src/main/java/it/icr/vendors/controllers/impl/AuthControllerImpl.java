package it.icr.vendors.controllers.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.vavr.control.Either;
import io.vavr.control.Try;
import it.icr.vendors.controllers.AuthController;
import it.icr.vendors.dto.VendorRegistrationDto;
import it.icr.vendors.entities.Vendor;
import it.icr.vendors.mappers.MapStructMapper;
import it.icr.vendors.repositories.VendorRepository;
import it.icr.vendors.requests.LoginRequest;
import it.icr.vendors.requests.auth.ResetPasswordRequest;
import it.icr.vendors.responses.LoginResponse;
import it.icr.vendors.responses.RegisterResponse;
import it.icr.vendors.responses.ResponseHandler;
import it.icr.vendors.services.AuthenticationService;
import it.icr.vendors.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthControllerImpl implements AuthController {
    @Autowired
    private MapStructMapper mapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private VendorRepository vendorrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<Object> login(@RequestBody  LoginRequest request) {
        Try<Vendor> authenticatedUser = authenticationService.authenticate(request);

        if (authenticatedUser.isFailure()) {
            return ResponseHandler.generateResponse("Credenziali invalide", HttpStatus.UNAUTHORIZED, null);
        }


        String jwtToken = jwtService.generateToken(authenticatedUser.get());

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        response.setVendor(mapper.vendorToVendorDto(authenticatedUser.get()));

        return ResponseEntity.ok(response);

    }


    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {

        Either<Error, Void> result = authenticationService.resetPassword(request);

        return result.fold(
                error -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                success -> ResponseEntity.ok().build()
        );
    }

    @Override
    public ResponseEntity<Void> testToken() {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Object> refreshToken(@RequestParam String old_token) {
        String email = null;
        try {
            email = jwtService.extractUsername(old_token);

        } catch (ExpiredJwtException e) {
            email = e.getClaims().getSubject();
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Qualcosa è andato storto", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
        Vendor vendor = vendorrepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utente non trovato"));

        String jwtToken = jwtService.generateToken(vendor);

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        response.setVendor(mapper.vendorToVendorDto(vendor));
        return ResponseHandler.generateResponse("Token aggiornato", HttpStatus.OK, response);

    }

    @Override
    public ResponseEntity<Object> register(String email){

        Either<Error, VendorRegistrationDto> result = authenticationService.register(email);

        if (result.isLeft()) {
            return ResponseHandler.generateResponse("Errore durante la registrazione", HttpStatus.BAD_REQUEST, null);
        }

        VendorRegistrationDto registrationResult = result.get();
        String plainPassword = registrationResult.getTempPassword();

        RegisterResponse response = new RegisterResponse();
        response.setTempPassword(plainPassword);
        response.setMessage("Utente creato correttamente");

        return ResponseEntity.ok(response);
    }
}
