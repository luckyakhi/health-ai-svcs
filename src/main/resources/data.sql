-- Seed a couple of users referenced by examples
MERGE INTO users (id, name) KEY(id) VALUES ('u1', 'Alex');
MERGE INTO users (id, name) KEY(id) VALUES ('u2', 'Sam');
