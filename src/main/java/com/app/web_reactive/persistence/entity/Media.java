package com.app.web_reactive.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
   private String title;
   private LocalDate release_date;
   private float average_rating;
   private boolean type;
}
