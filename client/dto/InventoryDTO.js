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
    getInvt_id() {
        return this.invt_id;
    }

    setInvt_id(invt_id) {
        this.invt_id = invt_id;
    }
    setBranch_id(branch_id) {
        this.branch_id = branch_id;
    }

    getDescription() {
        return this.description;
    }

    setDescription(description) {
        this.description = description;
    }

    getPicture() {
        return this.picture;
    }

    setPicture(picture) {
        this.picture = picture;
    }

    getQty() {
        return this.qty;
    }

    setQty(qty) {
        this.qty = qty;
    }

    getBought_price() {
        return this.bought_price;
    }

    setBought_price(bought_price) {
        this.bought_price = bought_price;
    }

    getSell_price() {
        return this.sell_price;
    }

    setSell_price(sell_price) {
        this.sell_price = sell_price;
    }

    getType() {
        return this.type;
    }

    setType(type) {
        this.type = type;
    }

    getSupplier_id() {
        return this.supplier_id;
    }

    setSupplier_id(supplier_id) {
        this.supplier_id = supplier_id;
    }

    // get invt_id() {
    //     return this.invt_id;
    // }
    //
    // set invt_id(value) {
    //     this.invt_id = value;
    // }
    //
    // get description() {
    //     return this.description;
    // }
    //
    // set description(value) {
    //     this.description = value;
    // }
    //
    // get picture() {
    //     return this.picture;
    // }
    //
    // set picture(value) {
    //     this.picture = value;
    // }
    //
    // get qty() {
    //     return this.qty;
    // }
    //
    // set qty(value) {
    //     this.qty = value;
    // }
    //
    // get bought_price() {
    //     return this.bought_price;
    // }
    //
    // set bought_price(value) {
    //     this.bought_price = value;
    // }
    //
    // get sell_price() {
    //     return this.sell_price;
    // }
    //
    // set sell_price(value) {
    //     this.sell_price = value;
    // }
    //
    // get type() {
    //     return this.type;
    // }
    //
    // set type(value) {
    //     this.type = value;
    // }
    //
    // get supplier_id() {
    //     return this.supplier_id;
    // }
    //
    // set supplier_id(value) {
    //     this.supplier_id = value;
    // }
}