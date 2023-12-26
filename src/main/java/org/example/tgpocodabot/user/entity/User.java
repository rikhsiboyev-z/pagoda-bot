package org.example.tgpocodabot.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "`user`")
public class User {
    @Id
    private Integer chatId;
    private String firstname;
    private UserState userState;
    private String userRegion;
}
