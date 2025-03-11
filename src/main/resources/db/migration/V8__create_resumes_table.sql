CREATE TABLE IF NOT EXISTS resumes
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL ,
    status VARCHAR(50) NOT NULL ,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    user_id BIGINT,
    job_id BIGINT,
    foreign key (user_id) references users(id),
    foreign key (job_id) references jobs(id)
)