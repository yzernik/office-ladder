# office-ladder schema
 
# --- !Ups
 
CREATE TABLE users (
    email           varchar(40) NOT NULL PRIMARY KEY,
    firstname       varchar(255) NOT NULL,
    lastname        varchar(255) NOT NULL,
    fullname        varchar(255) NOT NULL,
    providername    varchar(255) NOT NULL,
    providerkey     varchar(255) NOT NULL,
    created         timestamp NOT NULL
      
);

 
# --- !Downs
 
DROP TABLE users;