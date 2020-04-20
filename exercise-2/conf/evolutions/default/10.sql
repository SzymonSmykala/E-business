# --- !Ups
CREATE TABLE "product_question" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "product_id" INTEGER NOT NULL,
    "question_content" VARCHAR,
    FOREIGN KEY(product_id) references product(id)
)

# --- !Downs
DROP TABLE "product_question"
