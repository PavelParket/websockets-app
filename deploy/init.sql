create TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password TEXT NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at DATE NOT NULL
);

create unique index ux_users_email on users(email);

insert into users (username, email, password, role, created_at) values
  ('alice', 'alice@example.com', '$2b$12$RYexv2jB8EkIGudy5gCojOGJT0DwXFaJvrhzJdiQHODEIeqFVysrS', 'USER', '2025-09-05'),
  ('bob', 'bob@example.com', '$2b$12$ZS7t53.tNTZIOZt9g.doM.YagX4GB6lZfs0rb5Ko96B9PxNPziBH2', 'ADMIN', '2025-09-11'),
  ('charlie', 'charlie@example.com', '$2b$12$IyieV7lSehMWmA0.B8pLKueyCh8titK8N6SD09o7mMVoel8dyXVQq', 'USER', '2025-09-10'),
  ('pasha', 'pavel@gmail.com', '$2b$12$HjDs1Um6zIJl7EhXmgyCTuEfodVn.0V6yJLoA3GHXvcD4g8sm7Sbu', 'USER', '2025-09-08');
