-- Seed data: Default admin user
-- Password: admin123 (BCrypt encoded)
INSERT INTO
    users (
        full_name,
        email,
        password_hash,
        role,
        created_at,
        updated_at
    )
VALUES (
        'System Admin',
        'admin@attendly.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'ADMIN',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ) ON CONFLICT (email) DO NOTHING;