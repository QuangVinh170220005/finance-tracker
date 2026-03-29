package com.vinh.financetracker.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User extends BaseEntity{
    private String email;
    private String Password;
    private String fullName;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.LOCAL;
}
