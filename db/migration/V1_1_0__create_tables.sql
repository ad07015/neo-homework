CREATE TABLE IF NOT EXISTS country_phone_codes (
    id         integer,
    country    varchar(100),
    code       varchar(100),
    CONSTRAINT production UNIQUE(id, country)
);