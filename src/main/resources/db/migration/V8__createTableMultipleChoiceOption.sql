CREATE TABLE MultipleChoiceOption (
    id BIGINT NOT NULL AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    option_text VARCHAR(80) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    CONSTRAINT FK_MultipleChoiceOption_Task FOREIGN KEY (task_id) REFERENCES MultipleChoiceTask(id) ON DELETE CASCADE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;