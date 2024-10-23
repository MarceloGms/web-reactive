package com.app.web_reactive.data;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {
   private long id;
   private String name;
   private int age;
   private boolean gender;
}
