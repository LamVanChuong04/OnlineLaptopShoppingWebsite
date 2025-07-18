package com.fia.project_ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartDetails")
public class cartDetail {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;
 
     private long quantity;
 
     private double price;
 
     // cart_id: long
     @ManyToOne
     @JoinColumn(name = "cart_id")
     private cart cart;
 
     // product_id: long
     @ManyToOne
     @JoinColumn(name = "product_id")
     private Product product;
 
     public long getId() {
         return id;
     }
 
     public void setId(long id) {
         this.id = id;
     }
 
     public long getQuantity() {
         return quantity;
     }
 
     public void setQuantity(long quantity) {
         this.quantity = quantity;
     }
 
     public double getPrice() {
         return price;
     }
 
     public void setPrice(double price) {
         this.price = price;
     }
 
     public cart getCart() {
         return cart;
     }
 
     public void setCart(cart cart) {
         this.cart = cart;
     }
 
     public Product getProduct() {
         return product;
     }
 
     public void setProduct(Product product) {
         this.product = product;
     }
 
}
