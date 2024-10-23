package com.app.web_reactive.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.bind.annotation.*;

import com.app.web_reactive.data.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private DatabaseClient databaseClient;

    @PostMapping("/create")
    public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
        logger.info("Creating user: {}", user.getName());
        
        return databaseClient.sql("INSERT INTO users (name, age, gender) VALUES (:name, :age, :gender) RETURNING identifier")
            .bind("name", user.getName())
            .bind("age", user.getAge())
            .bind("gender", user.getGender())
            .map(row -> row.get("identifier", Long.class))
            .one()
            .map(id -> {
                user.setId(id);
                logger.info("User created successfully with ID: {}", id);
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            })
            .onErrorResume(e -> {
                logger.error("Error creating user: {}", e.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
            });
    }
    /*
    @GetMapping
    public List<Map<String, Object>> getAllUsers() {
        logger.info("Fetching all users");
        String sql = "SELECT id, name, email FROM users";
        return jdbcTemplate.queryForList(sql);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        String sql = "SELECT id, name, email FROM users WHERE id = ?";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, id);
        
        if (users.isEmpty()) {
            logger.warn("User not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users.get(0));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> user) {
        logger.info("Updating user with ID: {}", id);
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ? RETURNING id, name, email";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, user.get("name"), user.get("email"), id);
        
        if (result.isEmpty()) {
            logger.warn("User not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("User updated successfully with ID: {}", id);
        return ResponseEntity.ok(result.get(0));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Attempting to delete user with ID: {}", id);
        
        // First, check if the user is connected to any media
        String checkSql = "SELECT COUNT(*) FROM user_media_relationships WHERE user_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        
        if (count > 0) {
            logger.warn("Cannot delete user with ID: {} as they are connected to media", id);
            return ResponseEntity.badRequest().build();
        }
        
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        
        if (rowsAffected == 0) {
            logger.warn("User not found for deletion with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("User deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    } */
}
