# --- !Ups
CREATE TABLE "promo" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "product_id" INTEGER NOT NULL,
    "loose_amount" INTEGER ,
    FOREIGN KEY(product_id) references product(id)
)

# --- !Downs
DROP TABLE "promo"
