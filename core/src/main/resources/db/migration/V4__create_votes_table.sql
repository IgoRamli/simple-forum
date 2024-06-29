CREATE TABLE IF NOT EXISTS votes (
    user_id VARCHAR(36) NOT NULL,
    post_id INT NOT NULL,
    type VARCHAR(12) NOT NULL,
    PRIMARY KEY (post_id, user_id)
);