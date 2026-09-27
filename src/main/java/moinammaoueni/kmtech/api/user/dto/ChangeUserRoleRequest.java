package moinammaoueni.kmtech.api.user.dto;

import jakarta.validation.constraints.NotNull;
import moinammaoueni.kmtech.api.user.User;

public record ChangeUserRoleRequest(@NotNull User.Role role) {
}