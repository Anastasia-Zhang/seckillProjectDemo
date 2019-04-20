package com.miaoshaProject.controllor.viewobj;

import java.math.BigDecimal;
import java.util.List;

public class ShopCarVO {
    private List<ShopCarProductVO> ShopCarProductVoLists;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;

    public List<ShopCarProductVO> getShopCarProductVoLists() {
        return ShopCarProductVoLists;
    }

    public void setShopCarProductVoLists(List<ShopCarProductVO> shopCarProductVoLists) {
        ShopCarProductVoLists = shopCarProductVoLists;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
