CREATE TABLE public.invoices
(
    id         bigserial             NOT NULL,
    issue_date date                  NOT NULL,
    "number"   character varying(50) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.invoices
    ADD CONSTRAINT number_unique UNIQUE ("number");

ALTER TABLE public.invoices
    OWNER TO postgres;