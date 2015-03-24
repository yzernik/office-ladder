# migration adds ladders table

# --- !Ups
 
CREATE TABLE ladders (
    id              SERIAL PRIMARY KEY,
    name            varchar(255) NOT NULL,
    domain          varchar(255) NOT NULL,
    creator         varchar(40) NOT NULL REFERENCES users(email),
    created         timestamp NOT NULL
      
);

 
# --- !Downs
 
DROP TABLE ladders;