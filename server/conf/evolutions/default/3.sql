# Add another column to ladders
  
# --- !Ups
ALTER TABLE ladders ADD COLUMN "active" BOOLEAN NOT NULL DEFAULT FALSE;
 
# --- !Downs
ALTER TABLE ladders DROP "active";
