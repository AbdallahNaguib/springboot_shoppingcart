package com.example.shoppingcart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Data
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String usertype;

    public UserInfo(String username, String password, String usertype) {
        this.username = username;
        this.password = password;
        this.usertype = usertype;
    }

}
