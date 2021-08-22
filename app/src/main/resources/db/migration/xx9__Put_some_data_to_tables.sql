INSERT INTO public.cars (id, registration_number, personal_use)
VALUES (1, 'FGH-1234', true);

INSERT INTO public.cars (id, registration_number, personal_use)
VALUES (2, 'QWE-2344', true);

INSERT INTO public.cars (id, registration_number, personal_use)
VALUES (3, 'TRE-4567', true);

INSERT INTO public.cars (id, registration_number, personal_use)
VALUES (4, 'GWH-3445', true);


INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (1, '565-27-5668', '66395 Shelley Center', 'Thoughtstorm', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (2, '566-55-1416', '4704 Ridgeview Park', 'Aimbo', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (3, '677-12-7896', '630 Badeau Alley', 'Yadel', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (4, '364-24-1650', '23 Heath Terrace', 'Kazio', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (5, '627-76-1740', '17 Nancy Way', 'Yozio', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (6, '396-17-9474', '6046 Charing Cross Park', 'Wikizz', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (7, '241-26-5655', '34427 Rockefeller Plaza', 'Lazz', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (8, '917-64-3131', '2 Saint Paul Road', 'Jaxworks', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (9, '309-56-9310', '93889 Riverside Avenue', 'Jabbertype', 319.94, 514.57);

INSERT INTO public.companies (id, tax_identification_number, address, name, health_insurance, pension_insurance)
VALUES (10, '453-53-6360', '6901 Helena Pass', 'Yakidoo', 319.94, 514.57);


INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (1, '„Radio”', 1, 100.00, 23.00, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (2, 'Dell XX', 1, 3000.00, 690.00, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (3, 'Fuel', 120, 600.00, 138.00, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (4, 'Tv', 1, 2000.00, 460.00, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (5, 'Car Clean', 1, 2000.00, 460.00, 1, 1);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (6, 'Desk', 1, 100.00, 8.00, 2, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (7, 'Chair', 1, 230.00, 18.40, 2, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (8, 'Table', 1, 34.00, 7.82, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (9, 'Sofa', 1, 230.00, 0.00, 4, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (10, 'Printer', 2, 300.00, 0.00, 4, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (11, 'Speackres', 3, 50.00, 0.00, 4, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (12, 'Radiator', 1, 80.00, 18.40, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (13, 'Internet', 1, 50.00, 0.00, 5, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (14, 'Door', 1, 300.00, 24.00, 2, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (15, 'Car', 1, 20000.00, 1600.00, 2, 2);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (16, 'Tablet', 1, 100.00, 8.00, 2, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (17, 'SmartPhone', 1, 1000.00, 80.00, 2, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (18, 'Vacuum cleaner', 1, 120.00, 27.60, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (19, 'Elecrtic grill', 1, 1000.00, 230.00, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (20, 'Toster', 1, 300.00, 15.00, 3, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (21, 'Projector', 1, 2000.00, 100.00, 3, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (22, 'SmartWatch', 1, 500.00, 115.00, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (23, 'Camera', 1, 700.00, 35.00, 3, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (24, 'Keyboard', 1, 100.00, 0.00, 5, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (25, 'Mouse', 1, 50.00, 11.50, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (26, 'TvDecoder', 1, 500.00, 115.00, 1, null);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (27, 'Tires', 1, 700.00, 161.00, 1, 3);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (28, 'car service', 1, 400.00, 92.00, 1, 3);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (29, 'car wash', 1, 50.00, 4.00, 2, 4);

INSERT INTO public.invoice_entries (id, description, quantity, net_price, vat_value, vat_rate, expense_related_to_car)
VALUES (30, 'hdmi cable', 5, 250.00, 20.00, 2, null);


INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (1, '2021-10-14', '14/10/2021/1234', 1, 2);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (2, '2021-07-14', '14/07/2021/8568', 3, 4);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (3, '2021-07-15', '15/07/2021/2387', 5, 6);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (4, '2021-07-25', '25/07/2021/5685', 2, 5);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (5, '2021-07-29', '29/07/2021/5899', 7, 8);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (6, '2021-07-26', '26/07/2021/8525', 9, 10);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (7, '2021-08-04', '04/08/2021/2358', 10, 9);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (8, '2021-06-10', '10/06/2021/2568', 1, 10);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (9, '2021-02-10', '10/02/2021/8956', 5, 10);

INSERT INTO public.invoices (id, date, invoice_number, buyer, seller)
VALUES (10, '2021-01-08', '08/01/2021/2563', 10, 2);


INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (4, 1);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (3, 2);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (2, 3);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (2, 4);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (5, 5);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (6, 6);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (2, 7);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (2, 8);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (7, 9);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (7, 10);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (1, 11);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (7, 12);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (7, 13);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (7, 14);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (1, 15);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (8, 16);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (9, 17);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (9, 18);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (10, 19);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (10, 20);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (7, 21);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (1, 22);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (1, 23);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (1, 24);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (2, 25);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (2, 26);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (2, 27);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (4, 28);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (4, 29);

INSERT INTO public.invoice_invoice_entries (invoices_id, invoice_entry_id)
VALUES (1, 30);

