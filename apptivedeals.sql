/*
AF API:
1. Get product info by product ID
https://www.abercrombie.com/api/ecomm/a-us/product/list?productIds=11244493
https://www.hollisterco.com/api/ecomm/h-us/product/list?productIds=27043827
2. Get collection info by collection ID (For category hierarchy and collection products and their image)
https://anf.scene7.com/is/image/anf/KIC_123-9132-2530-100_prod1?$product-anf-v1$&wid=800&hei=1000
https://anf.scene7.com/is/image/anf/KIC_331-9513-1880-286_prod1?$product-anf-v1$&wid=800&hei=1000
https://www.abercrombie.com/api/ecomm/a-us/product/list?collectionIds=188670
3. Get products by category
https://www.abercrombie.com/shop/AjaxNavAPIResponseJSON?catalogId=10901&categoryId=12204&langId=-1&requestType=category&rows=90&sort=bestmatch&start=0&storeId=10051
https://www.hollisterco.com/shop/AjaxNavAPIResponseJSON?catalogId=10201&categoryId=12634&langId=-1&requestType=category&rows=90&sort=bestmatch&start=0&storeId=10251
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
  is_primary boolean,
  create_date timestamp,
  update_date timestamp
);
create unique index images_unique_idx on images (product_id, lower(url));
create unique index images_unique_primary_id on images (product_id, is_primary);

create table snapshot_detail (
  id bigserial primary key,
  product_id bigint references products (id),
  price_regular numeric,
  price_discount numeric,
  snapshot_id bigint,
  is_active boolean,
  create_date timestamp
);

create table snapshots (
  id bigint primary key,
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

truncate table brands cascade;
truncate table categories cascade;
truncate table genders cascade;
truncate table images cascade;
truncate table products cascade;
truncate table snapshot_detail cascade;
truncate table snapshots cascade;
*/

select 'product' as type, count(*) cnt from products
union
select 'gender' as type, count(*) cnt from genders
union
select 'category' as type, count(*) cnt from categories
union
select 'image' as type, count(*) cnt from images
union
select 'brand' as type, count(*) cnt from brands
union
select 'collection' as type, count(*) cnt from collections
union
select 'snapshot' as type, count(*) cnt from snapshots
order by 1;

select snapshot_id, count(*) cnt from snapshot_detail sd group by snapshot_id;
select * from snapshot_detail where snapshot_id = 2;
select * from products where id = 11385;
select id, create_date from snapshots;
