package moinammaoueni.kmtech.api.media.storage;

public enum MediaFolder {

    USERS("users"),

    ORGANIZATIONS("organizations"),

    PROJECTS("projects"),

    APPLICATIONS("applications");

    private final String value;

    MediaFolder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}