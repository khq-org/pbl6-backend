package com.backend.pbl6schoolsystem.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
public class CustomUser extends User {
    private Long userId;
    private String role;
    private String firstName;
    private String lastName;
    private Long schoolId;
    private String street;
    private String district;
    private String city;
    private Timestamp createdDate;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId, String role,
                      String firstName, String lastName, Long schoolId, String street, String district, String city, Timestamp createdDate) {
        super(username, password, authorities);
        this.userId = userId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.schoolId = schoolId;
        this.street = street;
        this.district = district;
        this.city = city;
        this.createdDate = createdDate;
    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                      boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long userId, String role, String firstName,
                      String lastName, Long schoolId, String street, String district, String city, Timestamp createdDate) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.schoolId = schoolId;
        this.street = street;
        this.district = district;
        this.city = city;
        this.createdDate = createdDate;
    }
}
