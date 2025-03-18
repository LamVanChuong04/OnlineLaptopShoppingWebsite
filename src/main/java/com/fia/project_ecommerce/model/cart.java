package com.fia.project_ecommerce.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "carts")
public class cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 0)
    private int sum;

    // user_id
    @OneToOne()
     @JoinColumn(name = "user_id")
    private User user;
    public User getUser() {
        return user;
     }
    public void setUser(User user) {
        this.user = user;
    }
    @OneToMany(mappedBy = "cart")
    List<cartDetail> cartDetail;

    public long getId() {
        return id;
    }

    public int getSum() {
        return sum;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void setCartDetail(List<cartDetail> cartDetail) {
        this.cartDetail = cartDetail;
    }

    public List<cartDetail> getCartDetail() {
        return cartDetail;
    }
    
}
