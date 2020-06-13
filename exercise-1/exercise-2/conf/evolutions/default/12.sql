# --- !Ups
ALTER TABLE "product"
ADD price INTEGER;

# --- !Downs
ALTER TABLE "product"
DROP COLUMN product;
