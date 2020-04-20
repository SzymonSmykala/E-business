# --- !Ups
CREATE TABLE "payment" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "status" VARCHAR
)

# --- !Downs
DROP TABLE "payment"
