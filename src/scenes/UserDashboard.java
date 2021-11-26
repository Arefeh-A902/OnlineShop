package scenes;

import functionality.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shop.*;
import users.User;

import java.util.ArrayList;


public class UserDashboard {

    public static void userDashboard(){
        VBox userDashboardLayout = new VBox(Main.space);
        userDashboardLayout.setAlignment(Pos.CENTER);
        Label title = new Label("DashBoard");
        Button currentCartButton = new Button("current cart");
        currentCartButton.setOnAction(e -> {
            Main.appData.currentUser.currentCart.verifyCart();
            displayCart(Main.appData.currentUser.currentCart, Main.userDashboardScene);
        });
//        Button favouritesButton = new Button("favourites");
//        favouritesButton.setOnAction(e -> {
////            displayProducts(Main.appData.currentUser.favouriteProducts);
//        });
        Button purchasesButton = new Button("purchases");
        purchasesButton.setOnAction(e -> {
            showPurchases(Main.appData.currentUser.getPurchases(), Main.userDashboardScene);
        });
        Button allProducts = new Button("see all products");
        allProducts.setOnAction(e -> {
            showAllProducts(Main.userDashboardScene);
        });
        Button logoutButton = new Button("logout");
        logoutButton.setOnAction(e -> {
            User.logout();
        });
        userDashboardLayout.getChildren().addAll(title, currentCartButton, purchasesButton, allProducts, logoutButton);
        Main.userDashboardScene = new Scene(userDashboardLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(Main.userDashboardScene);
    }


    public static void setDashboard(){
        if(Main.appData.currentUser != null){
            userDashboard();
        }else if(Main.appData.currentSeller != null){
            SellerDashboard.sellerDashboard();
        }else if(Main.appData.currentAdmin != null){
            AdminDashboard.adminDashboard();
        }
    }

    public static void displayCart(Cart cart, Scene prev){
        VBox displayCartLayout = new VBox(Main.space);
        displayCartLayout.setAlignment(Pos.CENTER);
        Label title = new Label("Cart");
        ObservableList<HBox> productsList = FXCollections.observableArrayList();
        for(CartProduct cartP: cart.cartProducts){
            HBox hbox = new HBox(Main.space);
            hbox.setAlignment(Pos.CENTER);
            Label productLabel = new Label(cartP.product.toString());
            Label priceLabel = new Label(cartP.product.price.toString());
            Label countLabel = new Label(cartP.count.toString());
            Button incProduct = new Button("+");
            incProduct.setOnAction(e -> {
                cartP.incCount();
                countLabel.setText(cartP.count.toString());
            });
            Button decProduct = new Button("-");
            decProduct.setOnAction(e -> {
                Product p = cartP.product;
                cartP.decCount();
                countLabel.setText(cartP.count.toString());
                if(!cart.hasProduct(p)){
                    displayCart(cart, prev);
                }
            });
            hbox.getChildren().addAll(productLabel, priceLabel, decProduct, countLabel, incProduct);
            productsList.add(hbox);
        }
        final ListView<HBox> listView = new ListView<>(productsList);
        listView.setMaxSize(350, 250);
        Button purchaseButton = new Button("purchase");
        purchaseButton.setOnAction(e -> {
            if(cart.cartProducts.size() != 0){
                showPurchase(cart.purchase(), prev);
            }
        });
        Button backButton = new Button("back");
        backButton.setOnAction(e -> {
            Main.window.setScene(prev);
        });
        displayCartLayout.getChildren().addAll(title, listView, purchaseButton, backButton);
        Scene displayCartScene = new Scene(displayCartLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(displayCartScene);
    }

    public static void showPurchase(Purchase purchase, Scene prev){
        VBox showPurchaseLayout = new VBox(Main.space);
        showPurchaseLayout.setAlignment(Pos.CENTER);
        Label idLabel = new Label(purchase.getId().toString());
        Label productCountLabel = new Label(purchase.productCount.toString());
        Label totalPriceLabel = new Label(purchase.totalPrice.toString());
        Label statusLabel = new Label(purchase.status.toString());
        HBox purchaseData = new HBox(Main.space);
        purchaseData.setAlignment(Pos.CENTER);
        purchaseData.getChildren().addAll(idLabel, productCountLabel, totalPriceLabel, statusLabel);
        ObservableList<HBox> productsList = FXCollections.observableArrayList();
        for(CartProduct cartP: purchase.cart.cartProducts){
            HBox hbox = new HBox(Main.space);
            hbox.setAlignment(Pos.CENTER);
            Label productLabel = new Label(cartP.product.toString());
            Label priceLabel = new Label(cartP.product.price.toString());
            Label countLabel = new Label(cartP.count.toString());
            hbox.getChildren().addAll(productLabel, priceLabel, countLabel);
            if(Main.appData.currentAdmin != null) {
                Label verificationStatus = new Label(cartP.status.toString());
                hbox.getChildren().add(verificationStatus);
            }
            productsList.add(hbox);
        }
        final ListView<HBox> listView = new ListView<>(productsList);
        listView.setMaxSize(350, 250);
        Button backButton = new Button("back");
        backButton.setOnAction(e -> {
            Main.window.setScene(prev);
        });
        showPurchaseLayout.getChildren().addAll(purchaseData, listView, backButton);
        Scene showPurchaseScene = new Scene(showPurchaseLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(showPurchaseScene);
    }

    public static void showPurchases(ArrayList<Purchase> purchases, Scene prev){
        VBox showUserPurchasesLayout = new VBox(Main.space);
        showUserPurchasesLayout.setAlignment(Pos.CENTER);
        Label title = new Label("Cart");
        ObservableList<HBox> purchasesList = FXCollections.observableArrayList();
        HBox headerHBox = new HBox(Main.space);
        headerHBox.setAlignment(Pos.CENTER);
        Label idLabel = new Label("id      ");
        Label totalPriceLabel = new Label("total price  ");
        Label statusLabel = new Label("status   ");
        Label detailLabel = new Label("detail   ");
        headerHBox.getChildren().addAll(idLabel, totalPriceLabel, statusLabel, detailLabel);
        purchasesList.add(headerHBox);
        for(Purchase purchase: purchases){
            HBox hbox = new HBox(Main.space);
            hbox.setAlignment(Pos.CENTER);
            Label id = new Label(purchase.getId().toString());
            Label totalPrice = new Label(purchase.totalPrice.toString());
            Label status = new Label(purchase.status.toString());
            hbox.getChildren().addAll(id, totalPrice, status);
            if(Main.appData.currentUser != null){
                Button detailButton = new Button("detail");
                detailButton.setOnAction(e -> {
                    showPurchase(purchase, prev);
                });
                hbox.getChildren().add(detailButton);
            }else if(Main.appData.currentAdmin != null){
                Button viewCartProductsStatus = new Button("view detail");
                viewCartProductsStatus.setOnAction(e -> {
                    showPurchase(purchase, prev);
                });
                hbox.getChildren().add(viewCartProductsStatus);
                if(purchase.status == PurchaseStatus.PENDING){
                    Button verifiedButton = new Button("verify");
                    verifiedButton.setOnAction(e -> {
                        Main.appData.currentAdmin.purchaseVerify(purchase);
                        showPurchases(purchases, prev);
                    });
                    hbox.getChildren().add(verifiedButton);
                }
                if(purchase.status == PurchaseStatus.VERIFIED){
                    Button deliveredButton = new Button("delivered");
                    deliveredButton.setOnAction(e -> {
                        Main.appData.currentAdmin.purchaseDelivering(purchase);
                        showPurchases(purchases, prev);
                    });
                    hbox.getChildren().add(deliveredButton);
                }
                if(purchase.status == PurchaseStatus.DELIVERING){
                    Button receivedButton = new Button("received");
                    receivedButton.setOnAction(e -> {
                        Main.appData.currentAdmin.purchaseRecieved(purchase);
                        showPurchases(purchases, prev);
                    });
                    hbox.getChildren().add(receivedButton);
                }
            }
            purchasesList.add(hbox);
        }
        final ListView<HBox> listView = new ListView<>(purchasesList);
        listView.setMaxSize(350, 250);
        Button backButton = new Button("back");
        backButton.setOnAction(e -> {
            Main.window.setScene(prev);
        });
        showUserPurchasesLayout.getChildren().addAll(title, listView, backButton);
        Scene showUserPurchasesScene = new Scene(showUserPurchasesLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(showUserPurchasesScene);
    }

    public static void showAllProducts(Scene prev){
        VBox showAllProductsLayout = new VBox(Main.space);
        showAllProductsLayout.setAlignment(Pos.CENTER);
        ObservableList<HBox> productsList = FXCollections.observableArrayList();
        for(Product product: Main.appData.products){
            HBox hbox = new HBox(Main.space);
            hbox.setAlignment(Pos.CENTER);
            Label name = new Label(product.name);
            Label price = new Label(product.price.toString());
            hbox.getChildren().addAll(name, price);
            if(Main.appData.currentUser != null){
                Cart cart = Main.appData.currentUser.currentCart;
                if(cart.getCartProduct(product) == null){
                    Button addToCart = new Button("add to cart");
                    addToCart.setOnAction(e -> {
                        Main.appData.currentUser.currentCart.addProduct(product);
                        showAllProducts(prev);
                    });
                    hbox.getChildren().add(addToCart);
                }else{
                    Label count = new Label(cart.getCartProduct(product).count.toString());
                    Button incProduct = new Button("+");
                    incProduct.setOnAction(e -> {
                        cart.addProduct(product);
                        count.setText(cart.getCartProduct(product).count.toString());
                    });
                    Button decProduct = new Button("-");
                    decProduct.setOnAction(e -> {
                        cart.removeOne(product);
                        if(!cart.hasProduct(product)){
                            showAllProducts(prev);
                            return;
                        }
                        count.setText(cart.getCartProduct(product).count.toString());
                    });
                    hbox.getChildren().addAll(decProduct, count, incProduct);
                }

            }else if(Main.appData.currentAdmin != null){
                //
            }
            productsList.add(hbox);
        }
        final ListView<HBox> listView = new ListView<>(productsList);
        listView.setMaxSize(350, 250);
        Button backButton = new Button("back");
        backButton.setOnAction(e -> {
            Main.window.setScene(prev);
        });
        showAllProductsLayout.getChildren().addAll(listView, backButton);
        Scene showAllProductsScene = new Scene(showAllProductsLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(showAllProductsScene);
    }

}
