CREATE TABLE IF NOT EXISTS jobs
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    salary DOUBLE,
    quantity INT,
    level VARCHAR(255),
    description LONGTEXT,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    company_id BIGINT,
    FOREIGN KEY (company_id) REFERENCES companies(id)
);