ALTER TABLE files
ADD COLUMN owner_id BIGINT;

ALTER TABLE files
ADD CONSTRAINT fk_file_owner
FOREIGN KEY (owner_id)
REFERENCES users(id);