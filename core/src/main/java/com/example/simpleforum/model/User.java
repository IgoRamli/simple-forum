package com.example.simpleforum.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@SQLRestriction(CreateUpdateDeleteTimestampedEntity.SOFT_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Data
@Builder
public class User extends CreateUpdateDeleteTimestampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;
}
