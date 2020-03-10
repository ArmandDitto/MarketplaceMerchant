package com.ditto.training.marketplaceformerchant.model;

import java.util.ArrayList;
import java.util.List;

public class ProductErrorRespone {
    List<String> productNameError = new ArrayList<>();
    List<String> productQtyError = new ArrayList<>();
    List<String> productPriceError = new ArrayList<>();
    List<String> productMerchantError = new ArrayList<>();
    List<String> productCategoryError = new ArrayList<>();

    public List<String> getProductNameError() {
        return productNameError;
    }

    public List<String> getProductQtyError() {
        return productQtyError;
    }

    public List<String> getProductPriceError() {
        return productPriceError;
    }

    public List<String> getProductMerchantError() {
        return productMerchantError;
    }

    public List<String> getProductCategoryError() {
        return productCategoryError;
    }
}


