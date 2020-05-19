# Phonebook schema

# --- !Ups
CREATE TABLE IF NOT EXISTS phonebook (
id SERIAL PRIMARY KEY,
name VARCHAR(20) NOT NULL DEFAULT NULL,
phonenumber VARCHAR(30) NOT NULL DEFAULT NULL
);

# --- !Downs
DROP TABLE phonebook