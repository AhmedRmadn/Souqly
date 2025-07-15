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


CREATE TABLE categories (
    category_id CHAR(36) PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    category_details TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    
    -- Constraints
    CHECK (CHAR_LENGTH(category_name) >= 3 AND CHAR_LENGTH(category_name) <= 50),
    CHECK (CHAR_LENGTH(category_details) >= 5 AND CHAR_LENGTH(category_details) <= 255)
);

CREATE TABLE products (
    product_id VARCHAR(36) PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    image_url VARCHAR(255),
    product_details TEXT NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    price DOUBLE NOT NULL CHECK (price >= 0),
    discount DOUBLE DEFAULT 0 CHECK (discount >= 0 AND discount <= 100),
    special_price DOUBLE NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    seller_id CHAR(36) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
	CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES categories(category_id),
	CONSTRAINT fk_product_seller
        FOREIGN KEY (seller_id) REFERENCES users(user_id)
);


