package com.ua.user.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String email;
}
