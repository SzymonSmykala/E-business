# --- !Ups
CREATE TABLE "product_description" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "description" VARCHAR,
    "product_id" INTEGER,
    FOREIGN KEY(product_id) references product(id)
)

# --- !Downs
DROP TABLE "product_description"
