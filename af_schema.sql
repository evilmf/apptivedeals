--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5 (Ubuntu 10.5-0ubuntu0.18.04)
-- Dumped by pg_dump version 10.5 (Ubuntu 10.5-0ubuntu0.18.04)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: populate_product_name_ts_vector(); Type: FUNCTION; Schema: public; Owner: af
--

CREATE FUNCTION public.populate_product_name_ts_vector() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        NEW.name_tsvector := to_tsvector('simple', NEW.name);
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.populate_product_name_ts_vector() OWNER TO af;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: brands; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.brands (
    id bigint NOT NULL,
    name text,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


ALTER TABLE public.brands OWNER TO af;

--
-- Name: brands_id_seq; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.brands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.brands_id_seq OWNER TO af;

--
-- Name: brands_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: af
--

ALTER SEQUENCE public.brands_id_seq OWNED BY public.brands.id;


--
-- Name: categories; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.categories (
    id bigint NOT NULL,
    name text,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


ALTER TABLE public.categories OWNER TO af;

--
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categories_id_seq OWNER TO af;

--
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: af
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- Name: collections; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.collections (
    id bigint NOT NULL,
    collection_id text,
    brand_id bigint,
    product_id bigint,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


ALTER TABLE public.collections OWNER TO af;

--
-- Name: collections_id_seq; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.collections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.collections_id_seq OWNER TO af;

--
-- Name: collections_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: af
--

ALTER SEQUENCE public.collections_id_seq OWNED BY public.collections.id;


--
-- Name: genders; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.genders (
    id bigint NOT NULL,
    name text,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


ALTER TABLE public.genders OWNER TO af;

--
-- Name: genders_id_seq; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.genders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genders_id_seq OWNER TO af;

--
-- Name: genders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: af
--

ALTER SEQUENCE public.genders_id_seq OWNED BY public.genders.id;


--
-- Name: images; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.images (
    id bigint NOT NULL,
    url text,
    product_id bigint,
    is_primary boolean,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


ALTER TABLE public.images OWNER TO af;

--
-- Name: images_id_seq; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.images_id_seq OWNER TO af;

--
-- Name: images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: af
--

ALTER SEQUENCE public.images_id_seq OWNED BY public.images.id;


--
-- Name: product_summary; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.product_summary (
    id bigint NOT NULL,
    price_min double precision,
    price_max double precision,
    image_id bigint,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


ALTER TABLE public.product_summary OWNER TO af;

--
-- Name: products; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    product_id text,
    name text,
    url text,
    brand_id bigint,
    gender_id bigint,
    category_id bigint,
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    name_tsvector tsvector
);


ALTER TABLE public.products OWNER TO af;

--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.products_id_seq OWNER TO af;

--
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: af
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- Name: seq_brand_id; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.seq_brand_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_brand_id OWNER TO af;

--
-- Name: seq_category_id; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.seq_category_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_category_id OWNER TO af;

--
-- Name: seq_gender_id; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.seq_gender_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_gender_id OWNER TO af;

--
-- Name: seq_image_id; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.seq_image_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_image_id OWNER TO af;

--
-- Name: seq_product_id; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.seq_product_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_product_id OWNER TO af;

--
-- Name: seq_snapshot_detail_id; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.seq_snapshot_detail_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_snapshot_detail_id OWNER TO af;

--
-- Name: seq_snapshot_id; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.seq_snapshot_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.seq_snapshot_id OWNER TO af;

--
-- Name: snapshot_detail; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.snapshot_detail (
    id bigint NOT NULL,
    product_id bigint,
    price_regular numeric,
    price_discount numeric,
    snapshot_id bigint,
    is_active boolean,
    create_date timestamp without time zone
);


ALTER TABLE public.snapshot_detail OWNER TO af;

--
-- Name: snapshot_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: af
--

CREATE SEQUENCE public.snapshot_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.snapshot_detail_id_seq OWNER TO af;

--
-- Name: snapshot_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: af
--

ALTER SEQUENCE public.snapshot_detail_id_seq OWNED BY public.snapshot_detail.id;


--
-- Name: snapshots; Type: TABLE; Schema: public; Owner: af
--

CREATE TABLE public.snapshots (
    id bigint NOT NULL,
    snapshot jsonb,
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    snapshot_date timestamp without time zone
);


ALTER TABLE public.snapshots OWNER TO af;

--
-- Name: brands id; Type: DEFAULT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.brands ALTER COLUMN id SET DEFAULT nextval('public.brands_id_seq'::regclass);


--
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- Name: collections id; Type: DEFAULT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.collections ALTER COLUMN id SET DEFAULT nextval('public.collections_id_seq'::regclass);


--
-- Name: genders id; Type: DEFAULT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.genders ALTER COLUMN id SET DEFAULT nextval('public.genders_id_seq'::regclass);


--
-- Name: images id; Type: DEFAULT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.images ALTER COLUMN id SET DEFAULT nextval('public.images_id_seq'::regclass);


--
-- Name: products id; Type: DEFAULT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- Name: snapshot_detail id; Type: DEFAULT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.snapshot_detail ALTER COLUMN id SET DEFAULT nextval('public.snapshot_detail_id_seq'::regclass);


--
-- Name: brands brands_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.brands
    ADD CONSTRAINT brands_pkey PRIMARY KEY (id);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: collections collections_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.collections
    ADD CONSTRAINT collections_pkey PRIMARY KEY (id);


--
-- Name: genders genders_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.genders
    ADD CONSTRAINT genders_pkey PRIMARY KEY (id);


--
-- Name: images images_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_pkey PRIMARY KEY (id);


--
-- Name: product_summary product_summary_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.product_summary
    ADD CONSTRAINT product_summary_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: snapshot_detail snapshot_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.snapshot_detail
    ADD CONSTRAINT snapshot_detail_pkey PRIMARY KEY (id);


--
-- Name: snapshots snapshots_pkey; Type: CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.snapshots
    ADD CONSTRAINT snapshots_pkey PRIMARY KEY (id);


--
-- Name: brands_name_unique_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE UNIQUE INDEX brands_name_unique_idx ON public.brands USING btree (lower(name));


--
-- Name: categories_name_unique_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE UNIQUE INDEX categories_name_unique_idx ON public.categories USING btree (lower(name));


--
-- Name: collections_unique_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE UNIQUE INDEX collections_unique_idx ON public.collections USING btree (lower(collection_id), brand_id, product_id);


--
-- Name: genders_name_unique_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE UNIQUE INDEX genders_name_unique_idx ON public.genders USING btree (lower(name));


--
-- Name: images_unique_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE UNIQUE INDEX images_unique_idx ON public.images USING btree (product_id, lower(url));


--
-- Name: images_unique_primary_id; Type: INDEX; Schema: public; Owner: af
--

CREATE UNIQUE INDEX images_unique_primary_id ON public.images USING btree (product_id, is_primary);


--
-- Name: products_name_ts_vector_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE INDEX products_name_ts_vector_idx ON public.products USING gist (name_tsvector);


--
-- Name: products_unique_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE UNIQUE INDEX products_unique_idx ON public.products USING btree (lower(product_id), brand_id);


--
-- Name: snapshot_detail_pid_price_discount_idx; Type: INDEX; Schema: public; Owner: af
--

CREATE INDEX snapshot_detail_pid_price_discount_idx ON public.snapshot_detail USING btree (product_id, price_discount);


--
-- Name: collections collections_brand_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.collections
    ADD CONSTRAINT collections_brand_id_fkey FOREIGN KEY (brand_id) REFERENCES public.brands(id);


--
-- Name: collections collections_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.collections
    ADD CONSTRAINT collections_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: images images_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: product_summary product_summary_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.product_summary
    ADD CONSTRAINT product_summary_id_fkey FOREIGN KEY (id) REFERENCES public.products(id);


--
-- Name: product_summary product_summary_image_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.product_summary
    ADD CONSTRAINT product_summary_image_id_fkey FOREIGN KEY (image_id) REFERENCES public.images(id);


--
-- Name: products products_brand_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_brand_id_fkey FOREIGN KEY (brand_id) REFERENCES public.brands(id);


--
-- Name: products products_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- Name: products products_gender_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_gender_id_fkey FOREIGN KEY (gender_id) REFERENCES public.genders(id);


--
-- Name: snapshot_detail snapshot_detail_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: af
--

ALTER TABLE ONLY public.snapshot_detail
    ADD CONSTRAINT snapshot_detail_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- PostgreSQL database dump complete
--

