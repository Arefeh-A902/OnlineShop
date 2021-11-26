package users;

import functionality.Main;
import shop.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Seller extends User {

//    public ArrayList<Product> products;
    private String password;

    public Seller(String username, String password) {
        super(username);
        this.password = password;
    }

    public ArrayList<Product> getProducts(){
        ArrayList<Product> products = new ArrayList<>();
        for(Product product: Main.appData.products){
            if(product.seller.equals(this)){
                products.add(product);
            }
        }
        return products;
    }

    public ArrayList<CartProduct> getUnverifiedProducts(){
        ArrayList<CartProduct> unverified = new ArrayList<>();
        for(Purchase purchase: Main.appData.purchases){
            if(purchase.status.equals(PurchaseStatus.PENDING)) {
                for (CartProduct cartP : purchase.cart.cartProducts) {
                    if (cartP.product.seller.equals(Main.appData.currentSeller) && cartP.status == CartProductStatus.PENDING) {
                        unverified.add(cartP);
                    }
                }
            }
        }
        return unverified;
    }

    public static Seller getByUsername(String username){
        for(Seller seller: Main.appData.sellers){
            if(seller.username.equals(username)){
                return seller;
            }
        }
        return null;
    }

    public static Boolean login(String username, String password){
        Seller tmpSeller = Seller.getByUsername(username);
        if(tmpSeller != null){
            if(tmpSeller.password.equals(password)){
                Main.appData.currentUser = null;
                Main.appData.currentSeller = tmpSeller;
                Main.appData.currentAdmin = null;
                System.out.println("Logged in successfully!");
                return true;
            }
        }
        return false;
    }

    public void delete(){
        this.isActive = Boolean.FALSE;
        for(Cart cart: Main.appData.carts){
            if(cart.status == CartStatus.PENDING){
                for(CartProduct cartP: cart.cartProducts){
                    if(cartP.product.seller.equals(this)){
                        cartP.status = CartProductStatus.DELETED;
                    }
                }
            }
        }
        for(Product product: Main.appData.products){
            if(product.seller.equals(this)){
                for(Cart cart: Main.appData.carts){
                    CartProduct cartP = cart.getCartProduct(product);
                    if(cartP != null){
                        cart.cartProducts.remove(cartP);
                    }
                }
            }
        }
    }

}
