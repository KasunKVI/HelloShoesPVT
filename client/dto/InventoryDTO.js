class InventoryDTO {
    constructor(invt_id, description, picture, qty, bought_price, sell_price, type, supplier_id) {
        this._invt_id = invt_id;
        this._description = description;
        this._picture = picture;
        this._qty = qty;
        this._bought_price = bought_price;
        this._sell_price = sell_price;
        this._type = type;
        this._supplier_id = supplier_id;
    }

    get invt_id() {
        return this._invt_id;
    }

    set invt_id(value) {
        this._invt_id = value;
    }

    get description() {
        return this._description;
    }

    set description(value) {
        this._description = value;
    }

    get picture() {
        return this._picture;
    }

    set picture(value) {
        this._picture = value;
    }

    get qty() {
        return this._qty;
    }

    set qty(value) {
        this._qty = value;
    }

    get bought_price() {
        return this._bought_price;
    }

    set bought_price(value) {
        this._bought_price = value;
    }

    get sell_price() {
        return this._sell_price;
    }

    set sell_price(value) {
        this._sell_price = value;
    }

    get type() {
        return this._type;
    }

    set type(value) {
        this._type = value;
    }

    get supplier_id() {
        return this._supplier_id;
    }

    set supplier_id(value) {
        this._supplier_id = value;
    }
}