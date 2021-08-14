CREATE TABLE public.invoice_invoice_entries
(
    invoices_id       bigint NOT NULL,
    invoice_entry_id bigint NOT NULL,
    PRIMARY KEY (invoices_id, invoice_entry_id)
);

ALTER TABLE public.invoice_invoice_entries
    ADD CONSTRAINT invoice_id_fk FOREIGN KEY (invoices_id)
        REFERENCES public.invoices (id);

ALTER TABLE public.invoice_invoice_entries
    ADD CONSTRAINT invoice_entry_id FOREIGN KEY (invoice_entry_id)
        REFERENCES public.invoice_entries (id);