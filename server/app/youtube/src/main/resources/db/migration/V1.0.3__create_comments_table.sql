CREATE TABLE video_comments(
    id INT AUTO_INCREMENT PRIMARY KEY,
    text text NOT NULL,
    parent_id INT,
    user_id INT NOT NULL,
    video_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES video_comments(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (video_id) REFERENCES videos(id)
)