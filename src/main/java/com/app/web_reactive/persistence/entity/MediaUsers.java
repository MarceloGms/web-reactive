package com.app.web_reactive.persistence.entity;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("media_users")
public class MediaUsers {

    private Long mediaIdentifier;

    private Long usersIdentifier;
}
