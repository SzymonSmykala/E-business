# --- !Ups

INSERT INTO "category"("name") VALUES("sample1");
INSERT INTO "category"("name") VALUES("sample2");
INSERT INTO "product"("name", "category_id") VALUES ("myName", 1)


# --- !Downs

DELETE FROM "category" WHERE name="sample1"";
DELETE FROM "category" WHERE name="sample2"";
