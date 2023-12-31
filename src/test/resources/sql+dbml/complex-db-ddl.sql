-- PUBLIC.BRAND definition

-- Drop table

-- DROP TABLE PUBLIC.BRAND;

CREATE TABLE PUBLIC.BRAND (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	DESCRIPTION CHARACTER VARYING(255),
	NAME CHARACTER VARYING(255),
	CONSTRAINT CONSTRAINT_3 PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_3 ON PUBLIC.BRAND (ID);


-- PUBLIC.COUNTRY definition

-- Drop table

-- DROP TABLE PUBLIC.COUNTRY;

CREATE TABLE PUBLIC.COUNTRY (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER(255) NOT NULL,
	CONSTRAINT CONSTRAINT_6 PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_6 ON PUBLIC.COUNTRY (ID);


-- PUBLIC.DISCOUNT definition

-- Drop table

-- DROP TABLE PUBLIC.DISCOUNT;

CREATE TABLE PUBLIC.DISCOUNT (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	CODE CHARACTER VARYING(255),
	END_DATE DATE,
	PERCENTAGE DOUBLE PRECISION NOT NULL,
	START_DATE DATE,
	CONSTRAINT CONSTRAINT_3E PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_3E ON PUBLIC.DISCOUNT (ID);


-- PUBLIC.MANUFACTURER definition

-- Drop table

-- DROP TABLE PUBLIC.MANUFACTURER;

CREATE TABLE PUBLIC.MANUFACTURER (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(32),
	CONSTRAINT CONSTRAINT_14 PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_14 ON PUBLIC.MANUFACTURER (ID);


-- PUBLIC.STATE definition

-- Drop table

-- DROP TABLE PUBLIC.STATE;

CREATE TABLE PUBLIC.STATE (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(255),
	CONSTRAINT CONSTRAINT_4B PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_4B ON PUBLIC.STATE (ID);


-- PUBLIC.SUPPLIER definition

-- Drop table

-- DROP TABLE PUBLIC.SUPPLIER;

CREATE TABLE PUBLIC.SUPPLIER (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(64),
	CONSTRAINT CONSTRAINT_CB PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_CB ON PUBLIC.SUPPLIER (ID);


-- PUBLIC.TAG definition

-- Drop table

-- DROP TABLE PUBLIC.TAG;

CREATE TABLE PUBLIC.TAG (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	DESCRIPTION CHARACTER VARYING(128),
	NAME CHARACTER VARYING(255),
	CONSTRAINT CONSTRAINT_143 PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_143 ON PUBLIC.TAG (ID);


-- PUBLIC.USER_PROFILE definition

-- Drop table

-- DROP TABLE PUBLIC.USER_PROFILE;

CREATE TABLE PUBLIC.USER_PROFILE (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	FIRST_NAME CHARACTER VARYING(255),
	LAST_NAME CHARACTER VARYING(255),
	CONSTRAINT CONSTRAINT_CE PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_CE ON PUBLIC.USER_PROFILE (ID);


-- PUBLIC.WAREHOUSE definition

-- Drop table

-- DROP TABLE PUBLIC.WAREHOUSE;

CREATE TABLE PUBLIC.WAREHOUSE (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	LOCATION CHARACTER VARYING(64),
	NAME CHARACTER VARYING(32),
	CONSTRAINT CONSTRAINT_2F PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX PRIMARY_KEY_2F ON PUBLIC.WAREHOUSE (ID);


-- PUBLIC.ADDRESS definition

-- Drop table

-- DROP TABLE PUBLIC.ADDRESS;

CREATE TABLE PUBLIC.ADDRESS (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	CITY CHARACTER VARYING(255),
	STREET CHARACTER VARYING(255),
	COUNTRY_ID BIGINT,
	PROFILE_ID BIGINT,
	STATE_ID BIGINT,
	CONSTRAINT CONSTRAINT_E PRIMARY KEY (ID),
	CONSTRAINT FK1Q744RIAGHKM2OSGU4MC31BXY FOREIGN KEY (PROFILE_ID) REFERENCES PUBLIC.USER_PROFILE(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FK4CX5JUGHTTW4H3QFXRCITBTXL FOREIGN KEY (STATE_ID) REFERENCES PUBLIC.STATE(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKE54X81NMCCSK5569HSJG1A6KA FOREIGN KEY (COUNTRY_ID) REFERENCES PUBLIC.COUNTRY(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FK1Q744RIAGHKM2OSGU4MC31BXY_INDEX_E ON PUBLIC.ADDRESS (PROFILE_ID);
CREATE INDEX FK4CX5JUGHTTW4H3QFXRCITBTXL_INDEX_E ON PUBLIC.ADDRESS (STATE_ID);
CREATE INDEX FKE54X81NMCCSK5569HSJG1A6KA_INDEX_E ON PUBLIC.ADDRESS (COUNTRY_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_E ON PUBLIC.ADDRESS (ID);


-- PUBLIC.CATEGORY definition

-- Drop table

-- DROP TABLE PUBLIC.CATEGORY;

CREATE TABLE PUBLIC.CATEGORY (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(255),
	PARENT_CATEGORY_ID BIGINT,
	CONSTRAINT CONSTRAINT_31 PRIMARY KEY (ID),
	CONSTRAINT FKS2RIDE9GVILXY2TCUV7WITNXC FOREIGN KEY (PARENT_CATEGORY_ID) REFERENCES PUBLIC.CATEGORY(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKS2RIDE9GVILXY2TCUV7WITNXC_INDEX_3 ON PUBLIC.CATEGORY (PARENT_CATEGORY_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_31 ON PUBLIC.CATEGORY (ID);


-- PUBLIC.CUSTOMER definition

-- Drop table

-- DROP TABLE PUBLIC.CUSTOMER;

CREATE TABLE PUBLIC.CUSTOMER (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	EMAIL CHARACTER VARYING(255),
	PASSWORD CHARACTER VARYING(255),
	USERNAME CHARACTER VARYING(255),
	PROFILE_ID BIGINT,
	CONSTRAINT CONSTRAINT_5 PRIMARY KEY (ID),
	CONSTRAINT FKO2QLYOCHXKBVN641BKMHUA5N6 FOREIGN KEY (PROFILE_ID) REFERENCES PUBLIC.USER_PROFILE(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKO2QLYOCHXKBVN641BKMHUA5N6_INDEX_5 ON PUBLIC.CUSTOMER (PROFILE_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_5 ON PUBLIC.CUSTOMER (ID);


-- PUBLIC.CUSTOMER_ORDER definition

-- Drop table

-- DROP TABLE PUBLIC.CUSTOMER_ORDER;

CREATE TABLE PUBLIC.CUSTOMER_ORDER (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	"DATE" CHARACTER VARYING(255),
	STATUS CHARACTER VARYING(255),
	TOTAL_AMOUNT NUMERIC(19,2),
	CUSTOMER_ID BIGINT,
	CONSTRAINT CONSTRAINT_3B PRIMARY KEY (ID),
	CONSTRAINT FKF9ABD30BHIQVUGAYXLPQ8RYQ9 FOREIGN KEY (CUSTOMER_ID) REFERENCES PUBLIC.CUSTOMER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKF9ABD30BHIQVUGAYXLPQ8RYQ9_INDEX_3 ON PUBLIC.CUSTOMER_ORDER (CUSTOMER_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_3B ON PUBLIC.CUSTOMER_ORDER (ID);


-- PUBLIC.PAYMENT definition

-- Drop table

-- DROP TABLE PUBLIC.PAYMENT;

CREATE TABLE PUBLIC.PAYMENT (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	AMOUNT NUMERIC(19,2),
	"DATE" CHARACTER VARYING(255),
	PAYMENT_TYPE CHARACTER VARYING(255),
	CUSTOMER_ORDER_ID BIGINT,
	CONSTRAINT CONSTRAINT_F PRIMARY KEY (ID),
	CONSTRAINT FK7705KOH3PAP3MBOJPTRY14KUO FOREIGN KEY (CUSTOMER_ORDER_ID) REFERENCES PUBLIC.CUSTOMER_ORDER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FK7705KOH3PAP3MBOJPTRY14KUO_INDEX_F ON PUBLIC.PAYMENT (CUSTOMER_ORDER_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_F ON PUBLIC.PAYMENT (ID);


-- PUBLIC.PAYMENT_DETAIL definition

-- Drop table

-- DROP TABLE PUBLIC.PAYMENT_DETAIL;

CREATE TABLE PUBLIC.PAYMENT_DETAIL (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	CREDIT_CARD_NUMBER CHARACTER VARYING(255),
	PROFILE_ID BIGINT,
	CONSTRAINT CONSTRAINT_C PRIMARY KEY (ID),
	CONSTRAINT FKPVAXU84FLI8Q4IKJUIJ8SRMNS FOREIGN KEY (PROFILE_ID) REFERENCES PUBLIC.USER_PROFILE(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKPVAXU84FLI8Q4IKJUIJ8SRMNS_INDEX_C ON PUBLIC.PAYMENT_DETAIL (PROFILE_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_C ON PUBLIC.PAYMENT_DETAIL (ID);


-- PUBLIC.PRODUCT definition

-- Drop table

-- DROP TABLE PUBLIC.PRODUCT;

CREATE TABLE PUBLIC.PRODUCT (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	DESCRIPTION CHARACTER VARYING(255),
	NAME CHARACTER VARYING(255),
	PRICE NUMERIC(19,2),
	BRAND_ID BIGINT,
	CATEGORY_ID BIGINT,
	CONSTRAINT CONSTRAINT_18 PRIMARY KEY (ID),
	CONSTRAINT FK1MTSBUR82FRN64DE7BALYMQ9S FOREIGN KEY (CATEGORY_ID) REFERENCES PUBLIC.CATEGORY(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKS6CYDSUALTSRPRVLF2BB3LCAM FOREIGN KEY (BRAND_ID) REFERENCES PUBLIC.BRAND(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FK1MTSBUR82FRN64DE7BALYMQ9S_INDEX_1 ON PUBLIC.PRODUCT (CATEGORY_ID);
CREATE INDEX FKS6CYDSUALTSRPRVLF2BB3LCAM_INDEX_1 ON PUBLIC.PRODUCT (BRAND_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_18 ON PUBLIC.PRODUCT (ID);


-- PUBLIC.PRODUCT_TAG definition

-- Drop table

-- DROP TABLE PUBLIC.PRODUCT_TAG;

CREATE TABLE PUBLIC.PRODUCT_TAG (
	PRODUCTS_ID BIGINT NOT NULL,
	TAGS_ID BIGINT NOT NULL,
	CONSTRAINT FKSGQXFNW7XB30IU6IV1J4WF9NB FOREIGN KEY (TAGS_ID) REFERENCES PUBLIC.TAG(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKTSX5TDY8XW568CS8DYKDTW2V FOREIGN KEY (PRODUCTS_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKSGQXFNW7XB30IU6IV1J4WF9NB_INDEX_2 ON PUBLIC.PRODUCT_TAG (TAGS_ID);
CREATE INDEX FKTSX5TDY8XW568CS8DYKDTW2V_INDEX_2 ON PUBLIC.PRODUCT_TAG (PRODUCTS_ID);


-- PUBLIC.REVIEW definition

-- Drop table

-- DROP TABLE PUBLIC.REVIEW;

CREATE TABLE PUBLIC.REVIEW (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	COMMENT CHARACTER VARYING(255),
	RATING INTEGER NOT NULL,
	CUSTOMER_ID BIGINT,
	PRODUCT_ID BIGINT,
	CONSTRAINT CONSTRAINT_8 PRIMARY KEY (ID),
	CONSTRAINT FKGCE54O0P6UUGOC2TEV4AWEWLY FOREIGN KEY (CUSTOMER_ID) REFERENCES PUBLIC.CUSTOMER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKIYOF1SINDB9QIQR9O8NPJ8KLT FOREIGN KEY (PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKGCE54O0P6UUGOC2TEV4AWEWLY_INDEX_8 ON PUBLIC.REVIEW (CUSTOMER_ID);
CREATE INDEX FKIYOF1SINDB9QIQR9O8NPJ8KLT_INDEX_8 ON PUBLIC.REVIEW (PRODUCT_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_8 ON PUBLIC.REVIEW (ID);


-- PUBLIC.SHIPMENT definition

-- Drop table

-- DROP TABLE PUBLIC.SHIPMENT;

CREATE TABLE PUBLIC.SHIPMENT (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STATUS CHARACTER VARYING(255),
	TRACKING_NUMBER CHARACTER VARYING(16),
	CUSTOMER_ORDER_ID BIGINT,
	CONSTRAINT CONSTRAINT_FD PRIMARY KEY (ID),
	CONSTRAINT FK7YOL7VK25I8JYS62UYBP4TSII FOREIGN KEY (CUSTOMER_ORDER_ID) REFERENCES PUBLIC.CUSTOMER_ORDER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FK7YOL7VK25I8JYS62UYBP4TSII_INDEX_F ON PUBLIC.SHIPMENT (CUSTOMER_ORDER_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_FD ON PUBLIC.SHIPMENT (ID);


-- PUBLIC.SUPPLIER_PRODUCT definition

-- Drop table

-- DROP TABLE PUBLIC.SUPPLIER_PRODUCT;

CREATE TABLE PUBLIC.SUPPLIER_PRODUCT (
	SUPPLIER_ID BIGINT NOT NULL,
	PRODUCTS_ID BIGINT NOT NULL,
	CONSTRAINT FKIELP9E4TVMQU204V3S6ODP1TC FOREIGN KEY (SUPPLIER_ID) REFERENCES PUBLIC.SUPPLIER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKNP3JOK9MHIX3KJ7K8X61KI94O FOREIGN KEY (PRODUCTS_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKIELP9E4TVMQU204V3S6ODP1TC_INDEX_C ON PUBLIC.SUPPLIER_PRODUCT (SUPPLIER_ID);
CREATE INDEX FKNP3JOK9MHIX3KJ7K8X61KI94O_INDEX_C ON PUBLIC.SUPPLIER_PRODUCT (PRODUCTS_ID);


-- PUBLIC.WISHLIST definition

-- Drop table

-- DROP TABLE PUBLIC.WISHLIST;

CREATE TABLE PUBLIC.WISHLIST (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	CUSTOMER_ID BIGINT,
	CONSTRAINT CONSTRAINT_F4 PRIMARY KEY (ID),
	CONSTRAINT FKB6XAK0RJUI1RSOK8LL7LN59CS FOREIGN KEY (CUSTOMER_ID) REFERENCES PUBLIC.CUSTOMER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKB6XAK0RJUI1RSOK8LL7LN59CS_INDEX_F ON PUBLIC.WISHLIST (CUSTOMER_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_F4 ON PUBLIC.WISHLIST (ID);


-- PUBLIC.WISHLIST_PRODUCT definition

-- Drop table

-- DROP TABLE PUBLIC.WISHLIST_PRODUCT;

CREATE TABLE PUBLIC.WISHLIST_PRODUCT (
	WISHLIST_ID BIGINT NOT NULL,
	PRODUCTS_ID BIGINT NOT NULL,
	CONSTRAINT FK6QI207S5P27BM3QMKXPK1FV8O FOREIGN KEY (WISHLIST_ID) REFERENCES PUBLIC.WISHLIST(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKIQKNANRNSI8QB4SNRAAW8R3SW FOREIGN KEY (PRODUCTS_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FK6QI207S5P27BM3QMKXPK1FV8O_INDEX_D ON PUBLIC.WISHLIST_PRODUCT (WISHLIST_ID);
CREATE INDEX FKIQKNANRNSI8QB4SNRAAW8R3SW_INDEX_D ON PUBLIC.WISHLIST_PRODUCT (PRODUCTS_ID);


-- PUBLIC.CART definition

-- Drop table

-- DROP TABLE PUBLIC.CART;

CREATE TABLE PUBLIC.CART (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	CUSTOMER_ID BIGINT,
	CONSTRAINT CONSTRAINT_1 PRIMARY KEY (ID),
	CONSTRAINT FKDEBWVAD6PP1EKIQY5JTIXQBAJ FOREIGN KEY (CUSTOMER_ID) REFERENCES PUBLIC.CUSTOMER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKDEBWVAD6PP1EKIQY5JTIXQBAJ_INDEX_1 ON PUBLIC.CART (CUSTOMER_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_1 ON PUBLIC.CART (ID);


-- PUBLIC.CART_ITEM definition

-- Drop table

-- DROP TABLE PUBLIC.CART_ITEM;

CREATE TABLE PUBLIC.CART_ITEM (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	QUANTITY INTEGER NOT NULL,
	CART_ID BIGINT,
	PRODUCT_ID BIGINT,
	CONSTRAINT CONSTRAINT_B PRIMARY KEY (ID),
	CONSTRAINT FK1UOBYHGL1WVGT1JPCCIA8XXS3 FOREIGN KEY (CART_ID) REFERENCES PUBLIC.CART(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKJCYD5WV4IGQNW413RGXBFU4NV FOREIGN KEY (PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FK1UOBYHGL1WVGT1JPCCIA8XXS3_INDEX_B ON PUBLIC.CART_ITEM (CART_ID);
CREATE INDEX FKJCYD5WV4IGQNW413RGXBFU4NV_INDEX_B ON PUBLIC.CART_ITEM (PRODUCT_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_B ON PUBLIC.CART_ITEM (ID);


-- PUBLIC.DISCOUNT_PRODUCT definition

-- Drop table

-- DROP TABLE PUBLIC.DISCOUNT_PRODUCT;

CREATE TABLE PUBLIC.DISCOUNT_PRODUCT (
	DISCOUNT_ID BIGINT NOT NULL,
	APPLICABLE_PRODUCTS_ID BIGINT NOT NULL,
	CONSTRAINT FKL3GH91M5P2LRKIQM7MLBS51PW FOREIGN KEY (APPLICABLE_PRODUCTS_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKMJAXUAJ7GVEYOGT1IPXNP7LWM FOREIGN KEY (DISCOUNT_ID) REFERENCES PUBLIC.DISCOUNT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKL3GH91M5P2LRKIQM7MLBS51PW_INDEX_B ON PUBLIC.DISCOUNT_PRODUCT (APPLICABLE_PRODUCTS_ID);
CREATE INDEX FKMJAXUAJ7GVEYOGT1IPXNP7LWM_INDEX_B ON PUBLIC.DISCOUNT_PRODUCT (DISCOUNT_ID);


-- PUBLIC.INVENTORY definition

-- Drop table

-- DROP TABLE PUBLIC.INVENTORY;

CREATE TABLE PUBLIC.INVENTORY (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	STOCK_COUNT INTEGER NOT NULL,
	PRODUCT_ID BIGINT,
	WAREHOUSE_ID BIGINT,
	CONSTRAINT CONSTRAINT_2 PRIMARY KEY (ID),
	CONSTRAINT FKIX9YXGETAU1Y25HHNV42GSIOK FOREIGN KEY (WAREHOUSE_ID) REFERENCES PUBLIC.WAREHOUSE(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKP7GJ4L80FX8V0UAP3B2CRJWP5 FOREIGN KEY (PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKIX9YXGETAU1Y25HHNV42GSIOK_INDEX_2 ON PUBLIC.INVENTORY (WAREHOUSE_ID);
CREATE INDEX FKP7GJ4L80FX8V0UAP3B2CRJWP5_INDEX_2 ON PUBLIC.INVENTORY (PRODUCT_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_2 ON PUBLIC.INVENTORY (ID);


-- PUBLIC.MANUFACTURER_PRODUCT definition

-- Drop table

-- DROP TABLE PUBLIC.MANUFACTURER_PRODUCT;

CREATE TABLE PUBLIC.MANUFACTURER_PRODUCT (
	MANUFACTURER_ID BIGINT NOT NULL,
	PRODUCTS_ID BIGINT NOT NULL,
	CONSTRAINT FKGKHME7PJVXHKWFL0WMB2TQMHE FOREIGN KEY (PRODUCTS_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FKROK4LNT42N4AOKJM7PIAI0MKS FOREIGN KEY (MANUFACTURER_ID) REFERENCES PUBLIC.MANUFACTURER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FKGKHME7PJVXHKWFL0WMB2TQMHE_INDEX_A ON PUBLIC.MANUFACTURER_PRODUCT (PRODUCTS_ID);
CREATE INDEX FKROK4LNT42N4AOKJM7PIAI0MKS_INDEX_A ON PUBLIC.MANUFACTURER_PRODUCT (MANUFACTURER_ID);


-- PUBLIC.ORDER_ITEM definition

-- Drop table

-- DROP TABLE PUBLIC.ORDER_ITEM;

CREATE TABLE PUBLIC.ORDER_ITEM (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	QUANTITY INTEGER NOT NULL,
	CUSTOMER_ORDER_ID BIGINT,
	PRODUCT_ID BIGINT,
	CONSTRAINT CONSTRAINT_4 PRIMARY KEY (ID),
	CONSTRAINT FK551LOSX9J75SS5D6BFSQVIJNA FOREIGN KEY (PRODUCT_ID) REFERENCES PUBLIC.PRODUCT(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FK7A0L2XEOPIENSBNHIXXDBOD07 FOREIGN KEY (CUSTOMER_ORDER_ID) REFERENCES PUBLIC.CUSTOMER_ORDER(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX FK551LOSX9J75SS5D6BFSQVIJNA_INDEX_4 ON PUBLIC.ORDER_ITEM (PRODUCT_ID);
CREATE INDEX FK7A0L2XEOPIENSBNHIXXDBOD07_INDEX_4 ON PUBLIC.ORDER_ITEM (CUSTOMER_ORDER_ID);
CREATE UNIQUE INDEX PRIMARY_KEY_4 ON PUBLIC.ORDER_ITEM (ID);