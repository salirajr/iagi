DROP SEQUENCE BOOKING_SEQ RESTRICT;
CREATE SEQUENCE BOOKING_SEQ AS BIGINT START WITH 0 MAXVALUE 999999 CYCLE;

SELECT T FROM (VALUES (NEXT VALUE FOR BOOKING_SEQ)) S(T);