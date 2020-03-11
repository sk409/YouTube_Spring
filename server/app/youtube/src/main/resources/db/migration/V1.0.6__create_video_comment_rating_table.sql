CREATE TABLE video_comment_rating(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    video_comment_id INT NOT NULL,
    rating_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (user_id, video_comment_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (video_comment_id) REFERENCES video_comments(id),
    FOREIGN KEY (rating_id) REFERENCES rating(id)
);