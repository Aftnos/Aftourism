DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    avatar VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1,
    remark VARCHAR(255),
    is_deleted TINYINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
