package com.app.web_reactive.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {
   @Id
   private long identifier;

   @NotBlank(message = "Name is mandatory")
   @Size(max = 512, message = "Name must be less than or equal to 512 characters")
   private String name;

   @Min(value = 0, message = "Age must be greater than or equal to 0")
   private int age;

   @Pattern(regexp = "M|F", message = "Gender must be 'M' or 'F'")
   private String gender;
}
