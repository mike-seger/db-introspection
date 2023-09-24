Table orders {
  order_id INT [pk]
  product_id INT
  quantity INT
}
Table products {
  product_id INT [pk]
  product_name VARCHAR
}
Ref: product_id > products.product_id