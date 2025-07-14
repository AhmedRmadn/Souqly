use souqly;
CREATE TABLE users (
    user_id CHAR(36) PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL CHECK (CHAR_LENGTH(first_name) >= 2),
    last_name VARCHAR(20) NOT NULL CHECK (CHAR_LENGTH(last_name) >= 2),
    user_name VARCHAR(50) NOT NULL UNIQUE CHECK (CHAR_LENGTH(user_name) BETWEEN 4 AND 50),
    email VARCHAR(50) NOT NULL UNIQUE CHECK (CHAR_LENGTH(email) >= 5),
    password VARCHAR(255) NOT NULL CHECK (CHAR_LENGTH(password) >= 8),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- Indexes for performance
CREATE INDEX idx_users_username ON users(user_name);
CREATE INDEX idx_users_email ON users(email);


CREATE TABLE roles (
    role_id CHAR(36) PRIMARY KEY,
    role_name ENUM('ADMIN', 'CUSTOMER', 'SELLER') NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    
    CONSTRAINT uq_role_name UNIQUE (role_name)
);


CREATE TABLE user_role (
    user_id CHAR(36) NOT NULL,
    role_id CHAR(36) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

-- Index for performance
CREATE INDEX idx_user_role_user_id ON user_role(user_id);

