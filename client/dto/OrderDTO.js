class OrderDTO {
    constructor(order_id, date, total, payment_method, points, customer_id, employee_id, branch_id, shoes, accessories) {
        this.order_id = order_id;
        this.date = date;
        this.total = total;
        this.payment_method = payment_method;
        this.points = points;
        this.customer_id = customer_id;
        this.employee_id = employee_id;
        this.branch_id = branch_id;
        this.shoes = shoes;
        this.accessories = accessories;
    }
}
