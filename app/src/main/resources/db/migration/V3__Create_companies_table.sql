CREATE TABLE public.companies
(
    id                        serial                 NOT NULL,
    tax_identification_number character varying(20)  NOT NULL,
    address                   character varying(100) NOT NULL,
    name                      character varying(100) NOT NULL,
    health_insurance          numeric(10, 2)         NOT NULL DEFAULT 0,
    pension_insurance         numeric(10, 2)         NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE public.companies
    ADD CONSTRAINT tin_unique UNIQUE (tax_identification_number);

ALTER TABLE public.companies
    OWNER to postgres;