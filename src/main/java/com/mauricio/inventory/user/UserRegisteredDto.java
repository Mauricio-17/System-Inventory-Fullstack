package com.mauricio.inventory.user;

import lombok.Data;

@Data
public
class UserRegisteredDto {

    private String username;
    private String password;
    private Long roleId;
}
