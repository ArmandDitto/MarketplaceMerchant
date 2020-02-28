package com.ditto.training.marketplaceformerchant.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int productId;
    private String productName;
    private String productSlug;
    private int productQty;
    private String productImage;
    private int productPrice;
    private String productDesc;
    private Merchant merchant;
    private Category category;

    public Product(int productId, String productName, String productSlug, int productQty, String productImage, int productPrice, String productDesc, Merchant merchant, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.productSlug = productSlug;
        this.productQty = productQty;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productDesc = productDesc;
        this.merchant = merchant;
        this.category = category;
    }

    protected Product(Parcel in) {
        productId = in.readInt();
        productName = in.readString();
        productSlug = in.readString();
        productQty = in.readInt();
        productImage = in.readString();
        productPrice = in.readInt();
        productDesc = in.readString();
        merchant = in.readParcelable(Merchant.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public int getProductQty() {
        return productQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeString(productName);
        dest.writeString(productSlug);
        dest.writeInt(productQty);
        dest.writeString(productImage);
        dest.writeInt(productPrice);
        dest.writeString(productDesc);
        dest.writeParcelable(merchant, flags);
        dest.writeParcelable(category, flags);
    }
}
