ALTER TABLE Task
ADD COLUMN type ENUM('OPEN_TEXT', 'SINGLE_CHOICE', 'MULTIPLE_CHOICE') NULL;

UPDATE Task t
JOIN OpenTextTask ot ON ot.id = t.id
SET t.type = 'OPEN_TEXT';

UPDATE Task t
JOIN SingleChoiceTask sct ON sct.id = t.id
SET t.type = 'SINGLE_CHOICE';

UPDATE Task t
JOIN MultipleChoiceTask mct ON mct.id = t.id
SET t.type = 'MULTIPLE_CHOICE';

ALTER TABLE Task
MODIFY COLUMN `type` ENUM('OPEN_TEXT', 'SINGLE_CHOICE', 'MULTIPLE_CHOICE') NOT NULL;
