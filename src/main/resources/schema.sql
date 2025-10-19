-- Portable DDL for users and tasks tables

-- USERS
CREATE TABLE IF NOT EXISTS users (
  id   VARCHAR(64)  NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

-- TASKS
CREATE TABLE IF NOT EXISTS tasks (
  id        VARCHAR(36)  NOT NULL PRIMARY KEY,
  title     VARCHAR(255) NOT NULL,
  status    VARCHAR(32)  NOT NULL,
  group_id  VARCHAR(255) NULL,
  owner_id  VARCHAR(64)  NULL,
  CONSTRAINT fk_tasks_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- Helpful indexes for filters
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_tasks_group  ON tasks(group_id);
CREATE INDEX IF NOT EXISTS idx_tasks_owner  ON tasks(owner_id);

