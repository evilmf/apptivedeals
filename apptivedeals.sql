/*
AF API:
1. Get product info by product ID
https://www.abercrombie.com/api/ecomm/a-us/product/list?productIds=11244493%2C19334845%2C24875325
2. Get collection info by collection ID (For category hierarchy and collection products and their image)
https://www.abercrombie.com/api/ecomm/a-us/product/list?collectionIds=188670
3. Get products by category
https://www.abercrombie.com/shop/AjaxNavAPIResponseJSON?catalogId=10901&categoryId=12204&langId=-1&requestType=category&rows=90&sort=bestmatch&start=0&storeId=10051
*/

create table brands (
  id bigserial primary key,
  name text,
  create_date timestamp,
  update_date timestamp
);
create unique index brands_name_unique_idx on brands (lower(name));

create table genders (
  id bigserial primary key,
  name text,
  create_date timestamp,
  update_date timestamp
);
create unique index genders_name_unique_idx on genders (lower(name));

create table categories (
  id bigserial primary key,
  name text,
  create_date timestamp,
  update_date timestamp
);
create unique index categories_name_unique_idx on categories (lower(name));

create table products (
  id bigserial primary key,
  product_id text,
  name text,
  url text,
  brand_id bigint references brands (id),
  gender_id bigint references genders (id),
  category_id bigint references categories (id),
  create_date timestamp,
  update_date timestamp,
  name_tsvector tsvector
);
create unique index products_unique_idx on products (lower(product_id), brand_id);

create table collections (
  id bigserial primary key,
  collection_id text,
  brand_id bigint references brands (id),
  product_id bigint references products (id),
  create_date timestamp,
  update_date timestamp
);
create unique index collections_unique_idx on collections (lower(collection_id), brand_id, product_id);

create table images (
  id bigserial primary key,
  url text,
  product_id bigint references products (id),
  create_date timestamp,
  update_date timestamp
);

create table snapshot_detail (
  id bigserial primary key,
  product_id bigint references products (id),
  price_regular numeric,
  price_discount numeric,
  snapshot_id bigint,
  create_date timestamp
);

create table snapshots (
  id bigserial primary key,
  snapshot jsonb,
  create_date timestamp,
  update_date timestamp
);

/*
drop table brands cascade;
drop table categories cascade;
drop table genders cascade;
drop table images cascade;
drop table products cascade;
drop table snapshot_detail cascade;
drop table snapshots cascade;
*/

/*
Queries
*/
--Brand
insert into brands (name, create_date, update_date)
values (:brand, now(), now())
on conflict (lower(name)) do update set id = EXCLUDED.id
returning id, name, create_date, update_date;

--Gender
insert into genders (name, create_date, update_date)
values (:gender, now(), now())
on conflict (lower(name)) do update set id = EXCLUDED.id
returning id, name, create_date, update_date;

--Category
insert into categories (name, create_date, update_date)
values (:category, now(), now())
on conflict (lower(name)) do update set id = EXCLUDED.id
returning id, name, create_date, update_date;

--Product
insert into products (product_id, name, url, brand_id, gender_id, category_id, create_date, update_date, name_tsvector)
values (:product_id, :name, :url, :brand_id, :gender_id, :category_id, now(), now(), to_tsvector('simple', :name))
on conflict (lower(product_id), brand_id) do update set id = EXCLUDED.id
returning id, product_id, name, url, brand_id, gender_id, category_id, create_date, update_date;

--Collection
insert into collections (collection_id, brand_id, product_id, create_date, update_date)
values (:collection_id, :brand_id, :product_id, now(), now())
on conflict (lower(collection_id), brand_id, product_id) do update set id = EXCLUDED.id
returning id, collection_id, brand_id, product_id, create_date, update_date;