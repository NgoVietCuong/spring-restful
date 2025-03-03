ALTER TABLE users
ADD COLUMN company_id BIGINT;

ALTER TABLE users
ADD CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES companies(id);