package hellojpa.seven.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;
}
