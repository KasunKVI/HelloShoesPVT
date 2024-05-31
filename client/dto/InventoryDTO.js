class InventoryDTO {
    constructor(invt_id, description, picture, qty, bought_price, sell_price, type, supplier_id) {
        this.invt_id = invt_id;
        this.description = description;
        this.picture = picture;
        this.qty = qty;
        this.bought_price = bought_price;
        this.sell_price = sell_price;
        this.type = type;
        this.supplier_id = supplier_id;
    }
}