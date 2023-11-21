--CREATE TABLE book
--(
--    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    author        VARCHAR(255)     NOT NULL,
--    seller_number VARCHAR(255)     NOT NULL,
--    product_type  VARCHAR(255)     NOT NULL,
--    price         DOUBLE PRECISION NOT NULL,
--    title         VARCHAR(255)     NOT NULL,
--    in_stock      INTEGER          NOT NULL,
--    CONSTRAINT pk_book PRIMARY KEY (id)
--);
--
--CREATE TABLE user_account
--(
--    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    login    VARCHAR(255) NOT NULL,
--    password VARCHAR(255) NOT NULL,
--    role     VARCHAR(50) NOT NULL,
--    CONSTRAINT pk_useraccount PRIMARY KEY (id)
--);
--
--CREATE TABLE goods_type
--(
--    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    name VARCHAR(255) NOT NULL,
--    CONSTRAINT pk_goodstype PRIMARY KEY (id)
--);
--
--CREATE TABLE cart
--(
--    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    CONSTRAINT pk_cart PRIMARY KEY (id)
--);
--
--CREATE TABLE cart_item
--(
--    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    goods_type_id BIGINT,
--    product_id BIGINT NOT NULL,
--    CONSTRAINT pk_cartitem PRIMARY KEY (id),
--    FOREIGN KEY (goods_type_id) REFERENCES goods_type (id)
--);
--
--CREATE TABLE cart_item_relation
--(
--    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    cart_id BIGINT,
--    cart_item_id BIGINT,
--    quantity INT NOT NULL,
--    CONSTRAINT pk_cartitemrelation PRIMARY KEY (id),
--    FOREIGN KEY (cart_id) REFERENCES cart(id),
--    FOREIGN KEY (cart_item_id) REFERENCES cart_item(id)
--);