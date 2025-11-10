DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS t_operation_log;

CREATE TABLE t_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trace_id VARCHAR(64),
    operator_id BIGINT,
    operator_type VARCHAR(20),
    module_name VARCHAR(100),
    operation_name VARCHAR(100),
    request_uri VARCHAR(255),
    request_method VARCHAR(10),
    class_method VARCHAR(255),
    request_params CLOB,
    response_body CLOB,
    success_flag TINYINT,
    error_msg VARCHAR(500),
    cost_ms INT,
    ip_address VARCHAR(50),
    user_agent VARCHAR(255)
);
