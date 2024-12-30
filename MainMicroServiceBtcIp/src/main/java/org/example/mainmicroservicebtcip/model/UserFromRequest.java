package org.example.mainmicroservicebtcip.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "password")  // Excluding password to avoid logging sensitive data
public class UserFromRequest {

    private String username;
    private String password;

}
