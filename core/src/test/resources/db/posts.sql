INSERT INTO posts
    (id, user_id, title, content, created_at, updated_at, deleted_at)
VALUES
    (1, 'user1', 'First Post', 'First content', '2024-06-02 00:00:00', '2024-06-02 00:00:00', NULL),
    (2, 'user2', 'Second Post', 'Second content', '2024-06-02 03:00:00', '2024-06-02 03:00:00', NULL),
    (3, 'user3', 'Third Post', 'Third content', '2024-06-02 04:00:00', '2024-06-02 04:00:00', '2024-06-02 18:00:00'),
    (4, 'user1', 'Another One', 'Just a reply to user2', '2024-06-03 02:00:00', '2024-06-03 02:00:00', NULL),
    (5, 'user2', 'Replying to Someone', 'Also a reply', '2024-06-03 05:00:00', '2024-06-02 05:00:00', NULL);