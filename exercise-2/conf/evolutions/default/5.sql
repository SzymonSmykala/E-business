# --- !Ups
CREATE TABLE "basket" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_id" INTEGER,
    FOREIGN KEY(user_id) references user(id)
)

# --- !Downs
DROP TABLE "basket"
