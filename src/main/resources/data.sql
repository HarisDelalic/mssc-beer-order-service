INSERT INTO beer_order_line (id, version, created_date, last_modified_date, beer_id, upc, order_quantity, quantity_allocated )
 values ('0a818933-087d-47f2-ad83-2f986ed087eb', 1, CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,
   '0a818933-087d-47f2-ad83-2f986ed087eb', '0631234200036', 20, 10);
INSERT INTO beer_order (id, version, created_date, last_modified_date, customer_ref, order_status, order_status_callback_url)
 values ('a712d914-61ea-4623-8bd0-32c0f6545bfd', 1, CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,  1, 0, '');
INSERT INTO customer (id, version, created_date, last_modified_date, customer_name, api_key )
 values ('026cc3c8-3a0c-4083-a05b-e908048c1b08', 1, CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 'dela',  '026cc3c8-3a0c-4083-a05b-e908048c1b08');

update BEER_ORDER set customer_id = '026cc3c8-3a0c-4083-a05b-e908048c1b08' where id = 'a712d914-61ea-4623-8bd0-32c0f6545bfd';
update BEER_ORDER_LINE set beer_order_id = 'a712d914-61ea-4623-8bd0-32c0f6545bfd' where id = '0a818933-087d-47f2-ad83-2f986ed087eb';