CREATE TABLE sessions (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    session_time DATETIME NOT NULL,
    conference_id INT NOT NULL,
    chair_id INT NOT NULL,
    FOREIGN KEY (conference_id) REFERENCES conferences(conference_id),
    FOREIGN key (chair_id) REFERENCES users(user_id)
);