# --- !Ups
CREATE TABLE "favorite_item" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_id" INTEGER NOT NULL,
    "product_id" INTEGER NOT NULL,
    FOREIGN KEY(user_id) references user(id),
    FOREIGN KEY (product_id) references product(id)
)

# --- !Downs
DROP TABLE "favorite_item"
