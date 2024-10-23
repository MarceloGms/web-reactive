package com.app.web_reactive.data;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Adicione isso para o construtor sem argumentos
@AllArgsConstructor
@Table("media")
public class Media implements Serializable {
   private Long id; // Alterado para Long
   private String title;
   private LocalDate release_date; // Alterado para LocalDate
   private float average_rating;
   private boolean type;
}
