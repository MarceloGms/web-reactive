package com.app.web_reactive.data;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Table("users")
public class User {
    long id;
    String name;
    int age;
    boolean gender;
}
