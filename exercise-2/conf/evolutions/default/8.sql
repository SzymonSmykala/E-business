# --- !Ups
CREATE TABLE "basket_item" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "basket_id" INTEGER NOT NULL,
    "product_id" INTEGER NOT NULL,
    "count" INTEGER,
    FOREIGN KEY(basket_id) references basket(id),
    FOREIGN KEY (product_id) references product(id)
)

# --- !Downs
DROP TABLE "basket_item"
