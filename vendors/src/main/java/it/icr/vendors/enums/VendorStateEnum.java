package it.icr.vendors.enums;

public enum VendorStateEnum {

    PENDING_REGISTRATION("PENDING_REGISTRATION"),
    DRAFT("DRAFT"),
    PENDING_APPROVAL("PENDING_APPROVAL");

    private final String state;

    VendorStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
