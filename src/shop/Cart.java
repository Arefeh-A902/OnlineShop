package shop;

import functionality.Main;
import users.User;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

public class Cart {

    static Long helpId = 1L;
    Long id;

    User client;
    ArrayList<CartProduct> products;
    public CartStatus status;

    public Cart(User user) {
        this.client = user;
        this. products = new ArrayList<>();
        this.status = CartStatus.PENDING;

        this.id = helpId;
        helpId += 1;
    }

    public void addProduct(Product product, Long count){
        if(count > product.inventory){
            System.out.println("Invalid number of Items, Total of " + product.inventory + " are left");
            return;
        }
        CartProduct cartProduct = new CartProduct(this, product, count);
    }

    public void editProduct(Product product, Long count){
        for(CartProduct cartP: products){
            if(cartP.equals(product)){
                if(count == 0){
                    products.remove(cartP);
                    break;
                }
                cartP.count = count;
            }
        }
    }

    public void removeProduct(Product product){
        editProduct(product, 0L);
    }

}
