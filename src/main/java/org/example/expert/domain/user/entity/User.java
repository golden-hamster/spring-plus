package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    private Long nicknameHash;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String imageUrl;


    public User(String email, String password, String nickname, Long nicknameHash, UserRole userRole, String imageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
        this.imageUrl = imageUrl;
        this.nicknameHash = nicknameHash;
    }

    private User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getId(), authUser.getEmail(), authUser.getUserRole());
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void updateNickname(String nickname, Long nicknameHash){
        this.nickname = nickname;
        this.nicknameHash = nicknameHash;
    }

    public void updateImageUrl(String imageUrl){ this.imageUrl = imageUrl;}

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
