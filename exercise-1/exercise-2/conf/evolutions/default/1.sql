# --- !Ups

CREATE TABLE "category" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL
);

CREATE TABLE "product" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "category_id" INTEGER NOT NULL
 FOREIGN KEY(category) references category(id)
);


# --- !Downs

DROP TABLE "category"
DROP TABLE "product"
