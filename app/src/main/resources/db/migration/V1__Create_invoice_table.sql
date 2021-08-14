CREATE TABLE public.invoices
(
    id             bigserial                NOT NULL,
    issue_date     date                  NOT NULL,
    invoice_number character varying(50) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.invoices
    ADD CONSTRAINT invoice_number_unique UNIQUE (invoice_number);