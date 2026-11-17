CREATE TABLE association_value_entry_seq (
    next_val BIGINT NOT NULL
);

INSERT INTO association_value_entry_seq (next_val) VALUES (1);

CREATE TABLE association_value_entry (
    id BIGINT NOT NULL,
    association_key VARCHAR(255),
    association_value VARCHAR(255),
    saga_id VARCHAR(255),
    saga_type VARCHAR(255),
    PRIMARY KEY (id),
    INDEX idx_association_value_entry_lookup (saga_type, association_key, association_value),
    INDEX idx_association_value_entry_saga (saga_id, saga_type)
);

CREATE TABLE saga_entry (
    saga_id VARCHAR(255) NOT NULL,
    saga_type VARCHAR(255),
    revision VARCHAR(255),
    serialized_saga LONGBLOB,
    PRIMARY KEY (saga_id)
);

CREATE TABLE token_entry (
    processor_name VARCHAR(255) NOT NULL,
    segment INT NOT NULL,
    token LONGBLOB,
    token_type VARCHAR(255),
    timestamp VARCHAR(255) NOT NULL,
    owner VARCHAR(255),
    PRIMARY KEY (processor_name, segment)
);
