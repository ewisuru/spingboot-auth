CREATE SCHEMA springsecurity
    AUTHORIZATION postgres;

COMMENT ON SCHEMA springsecurity
    IS 'this schema is to keep user details required for spring authentication';

create table springsecurity.user_info (
	id serial PRIMARY KEY,
	user_name 	varchar (50) unique not null,
	password varchar (50) not null,
	active boolean not null,
	roles varchar (100)
);