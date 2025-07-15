-- Insert Roles
INSERT INTO roles (role_id, role_name, created_at, updated_at) VALUES
('r-admin-0000-0000-0000-000000000000', 'ADMIN',    NOW(), NOW()),
('r-seller-0000-0000-0000-0000000000', 'SELLER',   NOW(), NOW()),
('r-customer-0000-0000-0000-000000000', 'CUSTOMER', NOW(), NOW());

-- Insert Users
INSERT INTO users (user_id, first_name, last_name, user_name, email, password, created_at, updated_at) VALUES
('u-admin-0000-0000-0000-000000000000', 'Alice', 'Admin', 'aliceadmin', 'alice@souqly.com',
 '$2a$10$2Vly3zGJrmeHdymZ4XY3E.DochBZ50imfmeJ7tdK8wwud/S4rU44u', NOW(), NOW()),
 
('u-seller-0000-0000-0000-00000000000', 'Bob', 'Seller', 'bobseller', 'bob@souqly.com',
 '$2a$10$Dow1NUQtNVXLdaNOI9rI6OluZKN5ch5XJePFC4XoVZIk9AXlMoYAS', NOW(), NOW()),

('u-customer-000-0000-0000-00000000000', 'Carol', 'Customer', 'carolcustomer', 'carol@souqly.com',
 '$2a$10$Dow1NUQtNVXLdaNOI9rI6OluZKN5ch5XJePFC4XoVZIk9AXlMoYAS', NOW(), NOW());

-- Assign Roles
INSERT INTO user_role (user_id, role_id, created_at, updated_at) VALUES
('u-admin-0000-0000-0000-000000000000', 'r-admin-0000-0000-0000-000000000000', NOW(), NOW()),
('u-seller-0000-0000-0000-00000000000', 'r-seller-0000-0000-0000-0000000000', NOW(), NOW()),
('u-customer-000-0000-0000-00000000000', 'r-customer-0000-0000-0000-000000000', NOW(), NOW());
