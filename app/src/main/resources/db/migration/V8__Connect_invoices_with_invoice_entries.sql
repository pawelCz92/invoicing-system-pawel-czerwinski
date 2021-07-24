CREATE TABLE public.invoices_invoice_entries
(
    invoice_id       bigint NOT NULL,
    invoice_entry_id bigint NOT NULL,
    PRIMARY KEY (invoice_id, invoice_entry_id)
);

ALTER TABLE public.invoices_invoice_entries
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoice_id)
        REFERENCES public.invoices (id);

ALTER TABLE public.invoices_invoice_entries
    ADD CONSTRAINT invoice_entry_id FOREIGN KEY (invoice_entry_id)
        REFERENCES public.invoices (id);

ALTER TABLE public.invoices_invoice_entries
    OWNER TO postgres;