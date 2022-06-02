package com.regin.reginald.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//import nl.elastique.poetry.json.annotations.MapFrom;

@DatabaseTable(tableName = "OrderHeaders")
public class Orders
{
    @DatabaseField(id = true, columnName = "id")
    //@MapFrom("id")
    private int id;

    @DatabaseField(columnName = "OrderId")
    //@MapFrom("OrderId")
    private String OrderId;

    @DatabaseField(columnName = "InvoiceNo")
   // @MapFrom("InvoiceNo")
    private String InvoiceNo;

    @DatabaseField(columnName = "CustomerPastelCode")
    //@MapFrom("CustomerPastelCode")
    private String CustomerPastelCode;

    @DatabaseField(columnName = "StoreName")
   // @MapFrom("StoreName")
    private String StoreName;

    @DatabaseField(columnName = "DeliveryDate")
   // @MapFrom("DeliveryDate")
    private String DeliveryDate;

    @DatabaseField(columnName = "DeliverySequence")
   // @MapFrom("DeliverySequence")
    private String DeliverySequence;

    @DatabaseField(columnName = "Latitude")
    //@MapFrom("Latitude")
    private String Latitude;

    @DatabaseField(columnName = "Longitude")
    //@MapFrom("Longitude")
    private String Longitude;

    @DatabaseField(columnName = "OrderValueExc")
   // @MapFrom("OrderValueExc")
    private String OrderValueExc;
    @DatabaseField(columnName = "OrderValueInc")
   // @MapFrom("OrderValueInc")
    private String OrderValueInc;

    @DatabaseField(columnName = "User")
    //@MapFrom("User")
    private String User;

    @DatabaseField(columnName = "DeliveryAddress")
   // @MapFrom("DeliveryAddress")
    private String DeliveryAddress;
    @DatabaseField(columnName = "OrderMass")
   // @MapFrom("OrderMass")
    private String OrderMass;

    @DatabaseField(columnName = "offloaded")
   // @MapFrom("offloaded")
    private String offloaded;

    @DatabaseField(columnName = "Uploaded")
   // @MapFrom("Uploaded")
    private String Uploaded;

    @DatabaseField(columnName = "imagestring")
   // @MapFrom("imagestring")
    private String imagestring;

    @DatabaseField(columnName = "CashPaid")
    //@MapFrom("CashPaid")
    private String CashPaid;
    @DatabaseField(columnName = "strNotesDrivers")
    //@MapFrom("strNotesDrivers")
    private String strNotesDrivers;

    @DatabaseField(columnName = "strEmailCustomer")
    //@MapFrom("strEmailCustomer")
    private String strEmailCustomer;
    @DatabaseField(columnName = "strCashsignature")
   // @MapFrom("strCashsignature")
    private String strCashsignature;


    @DatabaseField(columnName = "InvTotIncl")
    //MapFrom("InvTotIncl")
    private String InvTotIncl;

    @DatabaseField(columnName = "StartTripTime")
    //@MapFrom("StartTripTime")
    private String StartTripTime;

    @DatabaseField(columnName = "EndTripTime")
    //@MapFrom("EndTripTime")
    private String EndTripTime;

    @DatabaseField(columnName = "DeliverySeq")
    //@MapFrom("DeliverySeq")
    private int DeliverySeq;

    @DatabaseField(columnName = "strCoordinateStart")
  //  @MapFrom("strCoordinateStart")
    private String strCoordinateStart;

    @DatabaseField(columnName = "DriverName")
   // @MapFrom("DriverName")
    private String DriverName;
    //,DriverEmail TEXT,DriverPassword TEXT
    @DatabaseField(columnName = "DriverEmail")
  //  @MapFrom("DriverEmail")
    private String DriverEmail;

    @DatabaseField(columnName = "DriverPassword")
  //  @MapFrom("DriverPassword")
    private String DriverPassword;

    @DatabaseField(columnName = "strCustomerSignedBy")
   // @MapFrom("strCustomerSignedBy")
    private String strCustomerSignedBy;

    @DatabaseField(columnName = "PaymentType")
  //  @MapFrom("PaymentType")
    private String PaymentType;

    @DatabaseField(columnName = "Loyalty")
    //@MapFrom("Loyalty")
    private String Loyalty;

    @DatabaseField(columnName = "Threshold")
   // @MapFrom("Threshold")
    private String Threshold;

    public int getId(){ return id; }

    public void setId(int id) {
        this.id = id;
    }
    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }
    public void setInvoiceNo(String InvoiceNo) {
        this.InvoiceNo = InvoiceNo;
    }
    public void setCustomerPastelCode(String CustomerPastelCode) {
        this.CustomerPastelCode = CustomerPastelCode;
    }
    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }
    public void setDeliveryDate(String DeliveryDate) {
        this.DeliveryDate = DeliveryDate;
    }
    public void setDeliverySequence(String DeliverySequence) {
        this.DeliverySequence = DeliverySequence;
    }
    public void setDeliveryAddress(String DeliveryAddress) {
        this.DeliveryAddress = DeliveryAddress;
    }
    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }
    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }
    public void setOrderValueExc(String OrderValueExc) {
        this.OrderValueExc = OrderValueExc;
    }
    public void setOrderValueInc(String OrderValueInc) {
        this.OrderValueInc = OrderValueInc;
    }
    public void setUser(String User) {
        this.User = User;
    }
    public void setOffloaded(String offloaded) {
        this.offloaded = offloaded;
    }
    public void setOrderMass(String OrderMass) {
        this.OrderMass = OrderMass;
    }
    public void setImagestring(String imagestring) {
        this.imagestring = imagestring;
    }
    public void setCashPaid(String CashPaid) {
        this.CashPaid = CashPaid;
    }
    public void setstrNotesDrivers(String strNotesDrivers) {
        this.strNotesDrivers = strNotesDrivers;
    }
    public void setThreshold(String Threshold) {
        this.Threshold = Threshold;
    }
    public void setstrEmailCustomer(String strEmailCustomer) {
        this.strEmailCustomer = strEmailCustomer;
    }
    public void setstrCashsignature(String strCashsignature) {
        this.strCashsignature = strCashsignature;
    }
    public void setInvTotIncl(String InvTotIncl) {
        this.InvTotIncl = InvTotIncl;
    }
    public void setStartTripTime(String StartTripTime) {
        this.StartTripTime = StartTripTime;
    }
    public void setEndTripTime(String EndTripTime) {
        this.EndTripTime = EndTripTime;
    }
    public void setDeliverySeq(int DeliverySeq) {
        this.DeliverySeq = DeliverySeq;
    }
    public void setstrCoordinateStart(String strCoordinateStart) {
        this.strCoordinateStart = strCoordinateStart;
    }
    public void setDriverName(String DriverName) {
        this.DriverName = DriverName;
    }
    public void setDriverEmail(String DriverEmail) {
        this.DriverEmail = DriverEmail;
    }
    public void setDriverPassword(String DriverPassword) {
        this.DriverPassword = DriverPassword;
    }

    public void setstrCustomerSignedBy(String strCustomerSignedBy) {
        this.strCustomerSignedBy = strCustomerSignedBy;
    }
    public void setPaymentType(String PaymentType) {
            this.PaymentType = PaymentType;
    }
    public void setLoyalty(String Loyalty) {
            this.Loyalty = Loyalty;
    }

    public String getOrderId()
    {
        return OrderId;
    }
    public String getInvoiceNo()
    {
        return InvoiceNo;
    }
    public String getCustomerPastelCode() {
        return CustomerPastelCode;
    }
    public String getStoreName() {
        return StoreName;
    }
    public String getDeliveryDate() {
        return DeliveryDate;
    }
    public String getDeliverySequence() {
        return DeliverySequence;
    }
    public String getLatitude() {
        return Latitude;
    }
    public String getLongitude() {
        return Longitude;
    } public String getOrderValueExc() {
        return OrderValueExc;
    }
    public String getOrderValueInc() {
        return OrderValueInc;
    }
    public String getUser() {
        return User;
    }
    public String getDeliveryAddress() {
        return DeliveryAddress;
    }
    public String getOrderMass() {
        return OrderMass;
    }
    public String getoffloaded() {
        return offloaded;
    }
    public String getimagestring() {
        return imagestring;
    }
    public String getCashPaid() {
        return CashPaid;
    }
    public String getstrNotesDrivers() {
        return strNotesDrivers;
    }
    public String getstrEmailCustomer() {
        return strEmailCustomer;
    }
    public String getstrCashsignature() {
        return strCashsignature;
    }
    public String getInvTotIncl() {
        return InvTotIncl;
    }
    public String getStartTripTime() {
        return StartTripTime;
    }
    public String getEndTripTime() {
        return EndTripTime;
    }
    public int getDeliverySeq(){ return DeliverySeq; }
    public String getstrCoordinateStart(){ return strCoordinateStart; }
    public String getDriverName(){ return DriverName; }
    public String getDriverEmail(){ return DriverEmail; }
    public String getDriverPassword(){ return DriverPassword; }
    public String getstrCustomerSignedBy(){ return strCustomerSignedBy; }
    public String getPaymentType(){ return PaymentType; }
    public String getLoyalty(){ return Loyalty; }
    public String getThreshold(){ return Threshold; }

}
