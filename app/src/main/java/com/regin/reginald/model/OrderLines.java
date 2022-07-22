package com.regin.reginald.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


//@DatabaseTable(tableName = "OrderLines")
public class OrderLines {
    // @DatabaseField(id = true, columnName = "id")
    //@MapFrom("id")
    private int id;

    //@DatabaseField(columnName = "OrderId")
    // @MapFrom("OrderId")
    private String OrderId;

    // @DatabaseField(columnName = "ProductId")
    //@MapFrom("ProductId")
    private String ProductId;

    //  @DatabaseField(columnName = "PastelCode")
    // @MapFrom("PastelCode")
    private String PastelCode;

    // @DatabaseField(columnName = "PastelDescription")
    //@MapFrom("PastelDescription")
    private String PastelDescription;

    //  @DatabaseField(columnName = "Qty")
    //@MapFrom("Qty")
    private String Qty;

    //@DatabaseField(columnName = "Price")
    //@MapFrom("Price")
    private String Price;

    //  @DatabaseField(columnName = "OrderDetailId")
    // @MapFrom("OrderDetailId")
    private String OrderDetailId;

    // @DatabaseField(columnName = "Comment")
    //@MapFrom("Comment")
    private String Comment;

    // @DatabaseField(columnName = "offLoadComment")
    //@MapFrom("offLoadComment")
    private String offLoadComment;

    // @DatabaseField(columnName = "returnQty")
    // @MapFrom("returnQty")
    private String returnQty;

    // @DatabaseField(columnName = "Uploaded")
    //@MapFrom("Uploaded")
    private String Uploaded;

    // @DatabaseField(columnName = "blnoffloaded")
    //@MapFrom("blnoffloaded")
    private String blnoffloaded;

    //@DatabaseField(columnName = "strCustomerReason")
    //@MapFrom("strCustomerReason")
    private String strCustomerReason;

    //  @DatabaseField(columnName = "WareHouseName")
    //@MapFrom("WareHouseName")
    private String WareHouseName;

    //  @DatabaseField(columnName = "blnTruckchecked")
    //@MapFrom("blnTruckchecked")
    private int blnTruckchecked;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getOrderId() {
        return OrderId;
    }

    public String getProductId() {
        return ProductId;
    }

    public String getPastelCode() {
        return PastelCode;
    }

    public String getPastelDescription() {
        return PastelDescription;
    }

    public String getQty() {
        return Qty;
    }

    public String getPrice() {
        return Price;
    }

    public String getOrderDetailId() {
        return OrderDetailId;
    }

    public String getComment() {
        return Comment;
    }

    public String getoffLoadComment() {
        return offLoadComment;
    }

    public String getreturnQty() {
        return returnQty;
    }

    public String getblnoffloaded() {
        return blnoffloaded;
    }

    public String getstrCustomerReason() {
        return strCustomerReason;
    }

    public String getstrWareHouse() {
        return WareHouseName;
    }

    public int getblnTruckchecked() {
        return blnTruckchecked;
    }


    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public void setPastelCode(String PastelCode) {
        this.PastelCode = PastelCode;
    }

    public void setPastelDescription(String PastelDescription) {
        this.PastelDescription = PastelDescription;
    }

    public void setQty(String Qty) {
        this.Qty = Qty;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public void setOrderDetailId(String OrderDetailId) {
        this.OrderDetailId = OrderDetailId;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public void setoffLoadComment(String offLoadComment) {
        this.offLoadComment = offLoadComment;
    }

    public void setreturnQty(String returnQty) {
        this.returnQty = returnQty;
    }

    public void setblnoffloaded(String blnoffloaded) {
        this.blnoffloaded = blnoffloaded;
    }

    public void setstrCustomerReason(String strCustomerReason) {
        this.strCustomerReason = strCustomerReason;
    }

    public void setstrWareHouse(String strWareHouse) {
        this.WareHouseName = strWareHouse;
    }

    public void setblnTruckchecked(int blnTruckchecked) {
        this.blnTruckchecked = blnTruckchecked;
    }

}
