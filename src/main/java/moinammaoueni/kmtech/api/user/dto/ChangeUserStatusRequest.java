package moinammaoueni.kmtech.api.user.dto;

import jakarta.validation.constraints.NotNull;
import moinammaoueni.kmtech.api.user.User;

public record ChangeUserStatusRequest(@NotNull User.Status status) {}