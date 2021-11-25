package scenes;

import functionality.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shop.Cart;
import shop.CartProduct;
import shop.Product;
import users.User;

import java.util.ArrayList;

public class UserDashboard {

    public static void userDashboard(){
        VBox userDashboardLayout = new VBox(Main.space);
        userDashboardLayout.setAlignment(Pos.CENTER);
        Label title = new Label("DashBoard");
        Button currentCartButton = new Button("current cart");
        currentCartButton.setOnAction(e -> {
            displayCart(Main.appData.currentUser.currentCart);
        });
        Button nextCartButton = new Button("next cart");
        nextCartButton.setOnAction(e -> {
            displayCart(Main.appData.currentUser.nextCart);
        });
        Button favouritesButton = new Button("favourites");
        favouritesButton.setOnAction(e -> {
//            displayProducts(Main.appData.currentUser.favouriteProducts);
        });
        Button purchasesButton = new Button("purchases");
        purchasesButton.setOnAction(e -> {
            //TODO
        });
        Button allProducts = new Button("see all products");
        allProducts.setOnAction(e -> {
//            displayProducts(Main.appData.products);
        });
        Button logoutButton = new Button("logout");
        logoutButton.setOnAction(e -> {
            User.logout();
        });
        userDashboardLayout.getChildren().addAll(title, currentCartButton, nextCartButton, purchasesButton, allProducts, logoutButton);
        Main.userDashboardScene = new Scene(userDashboardLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(Main.userDashboardScene);
    }

    public static void sellerDashboard(){
        VBox sellerDashboardLayout = new VBox(Main.space);
        sellerDashboardLayout.setAlignment(Pos.CENTER);
        Label title = new Label("Dashboard");
        Button unverifiedProducts = new Button("unverified products");
        unverifiedProducts.setOnAction(e -> {
            showUnverifiedProducts();
        });
        Button allProductsButton = new Button("all products");
        allProductsButton.setOnAction(e -> {
//            showAllProudcts(1);
        });
        Button myProductsButton = new Button("my products");
        myProductsButton.setOnAction(e -> {

        });
        Button addProductButton = new Button("add product");
        addProductButton.setOnAction(e -> {
            createProductView(Main.sellerDashboardScene);
        });
        Button logoutButton = new Button("logout");
        logoutButton.setOnAction(e -> {
            User.logout();
        });
        sellerDashboardLayout.getChildren().addAll(title, unverifiedProducts, allProductsButton, myProductsButton, addProductButton, logoutButton);
        Main.sellerDashboardScene = new Scene(sellerDashboardLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(Main.sellerDashboardScene);
    }

    public static void setDashboard(){
        if(Main.appData.currentUser != null){
            userDashboard();
        }else if(Main.appData.currentSeller != null){
            sellerDashboard();
        }else if(Main.appData.currentAdmin != null){
//            adminDashboard();
        }
    }

    public static void createProductView(Scene prev){
        VBox createProductLayout = new VBox();
        createProductLayout.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Name");
        TextField name = new TextField();
        name.setMaxWidth((float)Main.screenWidth/4);
        Label priceLabel = new Label("Price");
        TextField price = new TextField();
        price.setMaxWidth((float)Main.screenWidth/4);
        Label inventoryLabel = new Label("Inventory");
        TextField inventory = new TextField();
        inventory.setMaxWidth((float)Main.screenWidth/4);
        Button addButton = new Button("add");
        Label emptyLabel = new Label("");
        addButton.setOnAction(e -> {
            for(Product p: Main.appData.products){
                System.out.println(p.toString());
            }
            emptyLabel.setText("");
            try{
                Long priceVar = Long.parseLong(price.getText());
                Long inventoryVar = Long.parseLong(inventory.getText());
                Main.appData.createProduct(name.getText(), priceVar, inventoryVar);
                name.clear();
                price.clear();
                inventory.clear();
                emptyLabel.setText("");
            }catch (NumberFormatException exception){
                emptyLabel.setText("invalid input!");
            }
        });
        Button backButton = new Button("back");
        backButton.setOnAction(e -> {
            Main.window.setScene(prev);
        });
        createProductLayout.getChildren().addAll(nameLabel, name, priceLabel, price, inventoryLabel, inventory);
        createProductLayout.getChildren().addAll(addButton, backButton, emptyLabel);
        Scene productView = new Scene(createProductLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(productView);
    }

    public static void showUnverifiedProducts(){

    }







    public static void displayCart(Cart cart){
        VBox displayCartLayout = new VBox(Main.space);
        displayCartLayout.setAlignment(Pos.CENTER);
        Label title = new Label("Cart");
        ObservableList<Button> products = FXCollections.observableArrayList();
        for(CartProduct cartP: cart.cartProducts) {
            Button tmp = new Button();
            tmp.setText(cartP.toString());
            tmp.setOnAction(e -> {
                displayCartProduct(cartP);
            });
            products.add(tmp);
        }
        ListView<Button> listView = new ListView<Button>(products);
        listView.setMaxSize(200, 160);
        Button backButton = new Button("back");
        backButton.setOnAction(e -> {
            Main.window.setScene(Main.userDashboardScene);
        });
        displayCartLayout.getChildren().addAll(title, listView, backButton);
        Scene displayCart = new Scene(displayCartLayout, Main.screenWidth, Main.screenHeight);
        Main.window.setScene(displayCart);
    }

    public static void displayProducts(int back){
        VBox displayProductsLayout = new VBox(Main.space);
        displayProductsLayout.setAlignment(Pos.CENTER);
        ObservableList<Button> productsList = FXCollections.observableArrayList();
//        for(Product product: products) {
//            Button tmp = new Button();
//            tmp.setText(product.toString());
//            tmp.setOnAction(e -> {
//                displayProduct(product);
//            });
//            products.add(tmp);
//        }
//        ListView<Button> listView = new ListView<Button>(products);
//        listView.setMaxSize(200, 160);

    }

    public static void displayCartProduct(CartProduct cartP){
        VBox displayProductLayout = new VBox(Main.space);
        displayProductLayout.setAlignment(Pos.CENTER);
        Label name = new Label(cartP.product.name);
        Label price = new Label(cartP.product.price.toString());
        Label count = new Label(cartP.count.toString());

        TextField newCount = new TextField();

        Button editButton = new Button("edit");
        editButton.setOnAction(e -> {
            cartP.cart.editProduct(cartP.product, Long.parseLong(newCount.getText()));
        });

        Button removeButton = new Button("remove");
        removeButton.setOnAction(e -> {
            cartP.cart.removeProduct(cartP.product);

        });


    }
}