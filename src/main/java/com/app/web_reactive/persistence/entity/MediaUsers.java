package com.app.web_reactive.persistence.entity;

import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("media_users")
public class MediaUsers {

    @NotNull(message = "Media identifier is mandatory")
    private Long mediaIdentifier;

    @NotNull(message = "User identifier is mandatory")
    private Long usersIdentifier;
}
