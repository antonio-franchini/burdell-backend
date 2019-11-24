-- Drop tables

drop TABLE IF EXISTS "user" CASCADE;
drop TABLE IF EXISTS profile CASCADE;
drop TABLE IF EXISTS vehicle CASCADE;
drop TABLE IF EXISTS color_mapping;
drop TABLE IF EXISTS business;
drop TABLE IF EXISTS individual;
drop TABLE IF EXISTS part CASCADE;
drop TABLE IF EXISTS part_order;
drop TABLE IF EXISTS parts_order;
drop TABLE IF EXISTS vendor;
drop TABLE IF EXISTS loan;
drop TABLE IF EXISTS manufaturer;
drop TABLE IF EXISTS vehicle_type;
drop TABLE IF EXISTS color;
drop TABLE IF EXISTS color_mapping;
drop TABLE IF EXISTS customer;



-- Enum tables

create table manufaturer
(
  manufacturer_name varchar(50) NOT NULL,
  PRIMARY KEY (manufacturer_name)
);

create table vehicle_type
(
  vehicle_type varchar(50) NOT NULL,
  PRIMARY KEY (vehicle_type)
);

create table color
(
  color varchar(50) NOT NULL,
  PRIMARY KEY (color)
);



-- Other tables

create table profile
(
  username   varchar(50)  NOT NULL,
  password   varchar(50)  NOT NULL,
  permission varchar(50)  NOT NULL,
  first_name varchar(255) NOT NULL,
  last_name  varchar(255) NOT NULL,
  PRIMARY KEY (username)
);

create table customer
(
  customer_id int         NOT NULL,
  email       varchar(50) NULL,
  phone       varchar(50) NOT NULL,
  street      varchar(50) NOT NULL,
  city        varchar(50) NOT NULL,
  state       varchar(50) NOT NULL,
  zip_code    varchar(50) NOT NULL,
  PRIMARY KEY (customer_id)
);


create table vehicle
(
  vin                  varchar(50)   NOT NULL,
  type                 varchar(50)   NOT NULL,
  make                 varchar(50)   NOT NULL,
  model                varchar(50)   NOT NULL,
  year                 int           NOT NULL,
  inventory_clerk_name varchar(50)   NOT NULL,
  saleperson_name      varchar(50)   NULL,
  mileage              int           NOT NULL,
  description          varchar(255)  NULL,
  purchase_price       numeric(10,2) NOT NULL,
  condition            varchar(50)   NOT NULL,
  purchase_date        date          NOT NULL,
  sale_date            date          NULL,
  buyer_customer_id    int           NULL,
  seller_customer_id   int           NOT NULL,
  PRIMARY KEY (vin),
  FOREIGN KEY (type)                 REFERENCES vehicle_type (vehicle_type),
  FOREIGN KEY (make)                 REFERENCES manufaturer (manufacturer_name),
  FOREIGN KEY (inventory_clerk_name) REFERENCES profile (username),
  FOREIGN KEY (saleperson_name)      REFERENCES profile (username),
  FOREIGN KEY (buyer_customer_id)    REFERENCES customer (customer_id),
  FOREIGN KEY (seller_customer_id)   REFERENCES customer (customer_id)
);


create table color_mapping
(
  vin   varchar(50) NOT NULL,
  color varchar(50) NOT NULL,
  CONSTRAINT color_mapping_pk PRIMARY KEY (vin, color)
);


create table business
(
  tax_id        varchar(50) NOT NULL,
  customer_id   serial,
  name          varchar(50) NOT NULL,
  contact_name  varchar(50) NOT NULL,
  contact_title varchar(50) NOT NULL,
  PRIMARY KEY (tax_id),
  FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);


create table individual
(
  driver_license varchar(50) NOT NULL,
  customer_id    int         NOT NULL,
  first_name     varchar(50) NOT NULL,
  last_name      varchar(50) NOT NULL,
  PRIMARY KEY (driver_license),
  FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);


create table vendor
(
  name     varchar(50) NOT NULL,
  phone    varchar(50) NOT NULL,
  street   varchar(50) NOT NULL,
  city     varchar(50) NOT NULL,
  state    varchar(50) NOT NULL,
  zip_code varchar(50) NOT NULL,
  PRIMARY KEY (name)
);


create table part
(
  part_number        varchar(255) NOT NULL,
  description        varchar(255) NOT NULL,
  PRIMARY KEY (part_number)
);


create table part_order
(
  part_order_number varchar(50)    not null,
  part_number       varchar(255)        not null
    constraint part_order_part_number_fkey
      references part,
  username          varchar(50)    not null
    constraint part_order_username_fkey
      references profile,
  vin               varchar(50)    not null
    constraint part_order_vin_fkey
      references vehicle,
  cost              numeric(10, 2) not null,
  status            varchar(50)    not null,
  vendor_name       varchar(50)    not null
    constraint part_order_vendor_name_fkey
      references vendor,
  batch_number      integer
);


create table loan
(
  start_month  date          NOT NULL,
  term         int           NOT NULL,
  payment      numeric(10,2) NOT NULL,
  interest     numeric(10,2) NOT NULL,
  down_payment numeric(10,2) NOT NULL,
  customer_id  int           NOT NULL,
  vin          varchar(50)   NOT NULL,
  CONSTRAINT loan_pk PRIMARY KEY (customer_id, vin),
  FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
  FOREIGN KEY (vin)         REFERENCES vehicle (vin)
);
