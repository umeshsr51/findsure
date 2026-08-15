-- FindSure schema notes (MySQL 8). Hibernate ddl-auto=update creates these in development.

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(120) NOT NULL, email VARCHAR(190) NOT NULL UNIQUE,
    phone VARCHAR(20), password_hash VARCHAR(255) NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'active',
    created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, user_id BIGINT NOT NULL, name VARCHAR(120) NOT NULL, category VARCHAR(60),
    description TEXT, photo_url VARCHAR(500), status VARCHAR(20) NOT NULL DEFAULT 'active', qr_token VARCHAR(20) NOT NULL UNIQUE,
    lost_at DATETIME, found_at DATETIME, deleted_at DATETIME, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL,
    CONSTRAINT fk_items_user FOREIGN KEY (user_id) REFERENCES users (id),
    INDEX idx_items_user_id (user_id), INDEX idx_items_qr_token (qr_token)
);

CREATE TABLE IF NOT EXISTS scans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, item_id BIGINT NOT NULL, latitude DOUBLE, longitude DOUBLE, approx_city VARCHAR(120),
    location_shared BOOLEAN NOT NULL DEFAULT FALSE, scanned_at DATETIME NOT NULL, ip_address VARCHAR(45), user_agent VARCHAR(255),
    CONSTRAINT fk_scans_item FOREIGN KEY (item_id) REFERENCES items (id)
);

CREATE TABLE IF NOT EXISTS finder_contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, scan_id BIGINT NOT NULL, name VARCHAR(120), email VARCHAR(190), phone VARCHAR(20),
    message TEXT NOT NULL, created_at DATETIME NOT NULL,
    CONSTRAINT fk_contacts_scan FOREIGN KEY (scan_id) REFERENCES scans (id)
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, user_id BIGINT NOT NULL, item_id BIGINT, scan_id BIGINT,
    type VARCHAR(40) NOT NULL, message VARCHAR(500) NOT NULL, is_read BOOLEAN NOT NULL DEFAULT FALSE, created_at DATETIME NOT NULL,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_notif_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_notif_scan FOREIGN KEY (scan_id) REFERENCES scans (id)
);
