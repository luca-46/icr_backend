package it.icr.vendors.requests.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String id;

    private String old_password;

    private String new_password;
}
