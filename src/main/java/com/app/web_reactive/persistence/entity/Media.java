package com.app.web_reactive.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("media")
public class Media implements Serializable {
   @Id
   private Long identifier;

   @NotBlank(message = "Title is mandatory")
   @Size(max = 512, message = "Title must be less than or equal to 512 characters")
   private String title;

   private LocalDate release_date;

   @Min(value = 0, message = "Average rating must be greater than or equal to 0")
   @Max(value = 10, message = "Average rating must be less than or equal to 10")
   private float average_rating;

   @NotNull(message = "Type is mandatory")
   private boolean type;
}
