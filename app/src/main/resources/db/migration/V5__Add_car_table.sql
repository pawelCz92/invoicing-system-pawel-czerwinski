CREATE TABLE public.cars
(
    id                  bigserial             NOT NULL,
    registration_number character varying(20) NOT NULL,
    personal_user       boolean               NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

ALTER TABLE public.cars
    OWNER TO postgres;