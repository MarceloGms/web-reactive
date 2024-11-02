-- Insert initial data into the media table
INSERT INTO media (title, release_date, average_rating, type) VALUES
('Media Title 1', '2023-01-01', 8.5, true),
('Media Title 2', '2023-02-01', 7.0, false),
('Media Title 3', '2023-03-01', 9.0, true),
('Media Title 4', '2023-04-01', 6.5, false),
('Media Title 5', '2023-05-01', 8.0, true),
('Media Title 6', '2023-06-01', 7.5, false),
('Media Title 7', '2023-07-01', 9.5, true),
('Media Title 8', '2023-08-01', 6.0, false),
('Media Title 9', '2023-09-01', 8.2, true),
('Media Title 10', '2023-10-01', 7.8, false);

-- Insert initial data into the users table
INSERT INTO users (name, age, gender) VALUES
('John Doe', 30, 'M'),
('Jane Doe', 25, 'F'),
('Alice Smith', 28, 'F'),
('Bob Johnson', 35, 'M'),
('Charlie Brown', 22, 'M'),
('Diana Prince', 27, 'F'),
('Eve Adams', 31, 'F'),
('Frank Castle', 40, 'M'),
('Grace Hopper', 29, 'F'),
('Hank Pym', 33, 'M');

-- Insert initial data into the media_users table
INSERT INTO media_users (media_identifier, users_identifier) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(3, 5),
(3, 6),
(4, 7),
(4, 8),
(5, 9),
(5, 10),
(6, 1),
(6, 3),
(7, 2),
(7, 4),
(8, 5),
(8, 7),
(9, 6),
(9, 8),
(10, 9),
(10, 10);
