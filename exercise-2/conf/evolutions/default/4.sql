# --- !Ups
CREATE TABLE "user" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "email" VARCHAR,
    "password" VARCHAR
)

# --- !Downs
DROP TABLE "user"
