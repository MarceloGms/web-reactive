INSERT INTO media (title, release_date, average_rating, type) 
VALUES 
('Movie 1', '2023-01-01', 8.5, TRUE), 
('Movie 2', '2022-05-15', 7.3, FALSE), 
('Movie 3', '2021-10-22', 9.1, TRUE);

INSERT INTO users (name, age, gender) 
VALUES 
('Alice', 25, TRUE), 
('Bob', 30, FALSE), 
('Charlie', 22, TRUE);

INSERT INTO media_users (media_identifier, users_identifier) 
VALUES 
(1, 1), -- Relaciona o 'Movie 1' com a 'Alice'
(2, 2), -- Relaciona o 'Movie 2' com o 'Bob'
(3, 3); -- Relaciona o 'Movie 3' com o 'Charlie'
