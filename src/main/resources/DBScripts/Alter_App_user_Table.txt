alter table app_user
add column "dial_code" varchar(5),
add column "country_code" varchar(5),
add column "e164_number" varchar(15),
add column "international_number" varchar(15),
add column "national_number" varchar(15),
add column "number" varchar(15);
alter table app_user 
add column "login_count" int default 0;

alter table app_user 
add column "overlay_url" varchar(100) NULL,
add column "overlay_type" varchar(15) NULL;