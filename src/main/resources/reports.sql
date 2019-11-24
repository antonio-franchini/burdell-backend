-- Seller history
select * from (SELECT a.name, b.seller_customer_id, COUNT(*) as total_vehicles_sold, avg(b.purchase_price) as average_vehicle_price FROM vehicle b
    JOIN
  (SELECT name, customer_id from business where customer_id in (SELECT seller_customer_id from vehicle)
   UNION
   SELECT concat(first_name, ' ', last_name) as name, customer_id from individual where customer_id in (SELECT seller_customer_id from vehicle)) a
  on (a.customer_id = b.seller_customer_id)
               GROUP BY a.name, b.seller_customer_id) top
join
              (select * from
(SELECT b.seller_customer_id,
       avg(b.number_of_parts_sold) as av_parts,
       avg(b.total_dollar_amount) as av_cost_parts
FROM
  (
    SELECT
      v.seller_customer_id,
      a.vin,
      COUNT(*) as number_of_parts_sold,
      avg(a.cost) as total_dollar_amount
    FROM
      part_order a
        join vehicle v on a.vin = v.vin
    GROUP BY
      a.vin, v.seller_customer_id
  ) b
group by b.seller_customer_id ) bot) z
on top.seller_customer_id = z.seller_customer_id;


-- Average time in inventory
SELECT
  type,
  avg(sale_date-purchase_date) as avg_days_in_inventory
FROM
  vehicle
GROUP BY
  type;


-- Price per condition
select type,
       round(avg((case when condition = 'Excellent' then purchase_price else 0 end)),2) as "excellent",
       round(avg((case when condition = 'Very Good' then purchase_price else 0 end)),2) as "very_good",
       round(avg((case when condition = 'Good' then purchase_price else 0 end)),2) as "good",
       round(avg((case when condition = 'Fair' then purchase_price else 0 end)),2) as "fair"
from vehicle
group by type;


-- Parts statistics
SELECT
  vendor_name,
  COUNT(*) as number_of_parts_sold,
  SUM(cost) as total_dollar_amount
FROM
  part_order
GROUP BY
  vendor_name;


-- Monthly loan income
SELECT
  start_month,
  sum(payment) as monthly_payment_total,
  sum(payment)/100 as monthly_share
FROM
  loan
where
  start_month > date_trunc('month', CURRENT_DATE) - INTERVAL '1 year'
GROUP BY
  start_month;


-- Monthly sales (part 1)
SELECT
  sale_date,
  COUNT(*) as vehicles_sold,
  SUM(purchase_price) * 1.25 as total_sales
FROM
  vehicle
WHERE
  sale_date notnull
GROUP BY
  sale_date;


-- Monthly sales (part 2)
SELECT
  saleperson_name,
  COUNT(*) as vehicles_sold,
  SUM(purchase_price)*1.25 as total_sales
FROM
  vehicle
WHERE
  sale_date notnull and sale_date = $sale_date
GROUP BY
  saleperson_name
ORDER BY vehicles_sold;