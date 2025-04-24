package com.countrym.deliveryservice.domain.user.entity;

import com.countrym.deliveryservice.common.config.security.UserInfo;
import com.countrym.deliveryservice.domain.auth.dto.request.SignUpRequestDto;
import com.countrym.deliveryservice.domain.user.enums.Authority;
import com.countrym.deliveryservice.domain.user.enums.Grade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private User(
            long id,
            Authority authority
    ) {
        this.id = id;
        this.authority = authority;
    }

    private User(
            String email,
            String password,
            String name,
            String nickname,
            boolean isOwner
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.grade = Grade.BRONZE;
        this.authority = Authority.from(isOwner);
    }

    public static User from(UserInfo userInfo) {
        return new User(
                userInfo.getId(),
                userInfo.getAuthority()
        );
    }

    public static User from(SignUpRequestDto signUpRequestDto, String encodedPassword) {
        return new User(
                signUpRequestDto.getEmail(),
                encodedPassword,
                signUpRequestDto.getName(),
                signUpRequestDto.getNickname(),
                signUpRequestDto.isOwner()
        );
    }

    public void deleteUser() {
        this.deletedAt = LocalDateTime.now();
    }
}
