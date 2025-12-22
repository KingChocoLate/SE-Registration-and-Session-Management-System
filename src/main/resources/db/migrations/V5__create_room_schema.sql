CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    capacity INT NOT NULL,
    location VARCHAR(100) NOT NULL
);
