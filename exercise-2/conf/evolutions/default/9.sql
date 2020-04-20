# --- !Ups
CREATE TABLE "order" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "basket_id" INTEGER NOT NULL,
    "payment_id" INTEGER NOT NULL,
    FOREIGN KEY(basket_id) references basket(id),
    FOREIGN KEY (payment_id) references payment(id)
)

# --- !Downs
DROP TABLE "order"
