package com.shopping.gway_4u;

public class config_hosts {

    public  final String hostname = "https://engravedesigns.in/";

    public final  String BASE_URL = "https://engravedesigns.in";

    public final String home = hostname+"index.php?route=api/home";

    public final String cart = hostname+"index.php?route=api/cart";



    public final  String wishlist_remove = hostname+"index.php?route=api/wishlist&remove="; //GET METHOD


    //PRIVACY POLICY
    public final String privacyPolicy = hostname+"index.php?route=information/information/agree&information_id=3";

    //TERMS & CONDITIONS
    public final String TERMSC = hostname+"index.php?route=information/information/agree&information_id=5";



    public final String login = hostname+"index.php?route=api/signin"; //POST AND SET TOKEN

    public final String logout = hostname+"index.php?route=api/signin/logout"; //POST WITH TOKEN


    // WISHLIST

    public  final String wishlists = hostname+"index.php?route=api/wishlist";

    public  final String wishlist_rm = hostname+"index.php?route=api/wishlist&remove=";

    public final String wishlist = hostname+"index.php?route=api/wishlist/add";

    public final String WISHLIST_ADD = hostname+"index.php?route=api/wishlist/add";


    //UPLOAD FILE
    public final  String FILE_UPLOAD = hostname+"index.php?route=tool/upload";


    //FEATURED
    public  final String FEATURED = hostname+"index.php?route=api/featured";

    //LATEST
    public  final String LATEST = hostname+"index.php?route=api/latest";


    //SERACH / FIND PRODUCT ESPECIALLY ON CART
    public  final String FIND_PRODUCT = hostname+"index.php?route=api/product&product_id=";


    //SLIDER IMAGE FRONT
    public  final  String SLIDER_IMAGE = hostname+"index.php?route=api/slideshow";


    //Notification Count
    public  final String COUNTS = hostname+"index.php?route=api/counts";

    //forgotten  password //POST METHOD
    public  final String FORGOTTEN = hostname+"index.php?route=api/forgotten";

    //forgotten  password //POST METHOD
    public  final String RESET = hostname+"index.php?route=api/reset";

    //change account password

    public  final String CHANGE_PASSWORDS = hostname+"index.php?route=api/password";



    //CART

    public   final String carts =  hostname+"index.php?route=api/cart";

    public final String cart_add = hostname+"index.php?route=api/cart/add";

    public final String removeCart = hostname+"index.php?route=api/cart/remove";


    //coupon POST
    public final String coupon = hostname+"index.php?route=api/coupon";


    //Payment Address
    public final String paymentAddress = hostname+"index.php?route=api/payment";

    //Delete Payment Address POST METHOD
    public final String DeletepaymentAddress = hostname+"index.php?route=api/payment/delete";



    //Store Billing Address IN db
    public final String billingAdresSave = hostname+"index.php?route=api/payment_address/save"; //POST


    //Store Billing address as Cookie

    public final String billingAdreesCookie = hostname+"index.php?route=api/payment_address"; //POST


    //save Shipping address
    public final String shippingAddressSave = hostname+"index.php?route=api/shipping_address/save";

    //Retreive current user Shipping Address

    public final String shippingAddress = hostname+"index.php?route=api/shipping_address";


    //Save Address in My account
    //METHOD = POST

    public final String MY_ACCOUNT_ADDR_SAVE = hostname+"index.php?route=api/myaccount/saveAddress";

    //Show Address in My account
    //METHOD = GET

    public final String MY_ACCOUNT_ADDR_SHOW = hostname+"index.php?route=api/myaccount/showAddress";

    //GET Address By ID for editing purpose

    public final String MY_ACCOUNT_ADDR_BY_ID = hostname+"index.php?route=api/myaccount/getAddressById";

    //Update Address By ID
    //METHOD = POST
    public final String MY_ACCOUNT_ADDR_BY_UPDATE = hostname+"index.php?route=api/myaccount/updateAddressById";




    //Save Shipping method
    public final String shippingSave = hostname+"index.php?route=api/shipping_method/save";


    //Retrieve and Store Shipping Method as Cookie

    public final String shippingURL = hostname+"index.php?route=api/shipping_method";



    //Payment methods
    public final String paymentMethod = hostname+"index.php?route=api/payment_method";

    //set Payment Method
    public final String setPayment = hostname+"index.php?route=api/payment_method/save";



    //Confirm Orders
    public final  String confirmOrder = hostname+"index.php?route=api/confirm";



    //Order COD LINK
    public final  String OrderCOD = hostname+"index.php?route=extension/payment/cod/confirm";



    //Order Success
    public final String successOrder = hostname+"index.php?route=api/success";



    //Instamojo Payment Gateway Link
    public final String instamojo = hostname+"index.php?route=api/instamojo/start";


    //Orders Link
    public final String orders = hostname+"index.php?route=api/orders";


    //get country lists
    public final String country = hostname+"index.php?route=api/country";

    //get State list by Country id = ""
    public final String states = hostname+"index.php?route=api/country/country&country_id=";


    //Search Product link
    //Accepts Param : search , sort , order ,
    //Ex search=iphone , sort=p.price , order=ASC OR DESC <- Must be capital letter
    //METHOD = GET
    public final String searchBy = hostname+"index.php?route=api/search&";



    //Edit Account Details
    public final String accountDetails = hostname+"index.php?route=api/accounts";


    //show returns
    public final String productreturns = hostname+"index.php?route=api/returns";



    //Order Info
    public final String orderInfo = hostname+"index.php?route=api/orderinfo/info&order_id=";


    //Downloads

    public final String downloads = hostname+"index.php?route=api/downloads";


    //Download Image

    public final String downloadimage = hostname+"index.php?route=api/preview/download";


    //Review Submission //POST

    public final String SUBMIT_FEEDBACK = hostname+"index.php?route=api/feedback/write";


    //GET Product Details from ORDER_ID GET method &order_id=xxxx
    public final String GET_PRODUCT_ID = hostname+"index.php?route=api/feedback";


    //Get Currency List GET

    public final String getCurrency = hostname+"index.php?route=api/curruncy";


    //Update Currency POST

    public final String updateCurrency = hostname+"index.php?route=api/curruncy/currency";

    //Track Order
    public final String TrackOrder = hostname+"index.php?route=api/track&order_id=";


    //Registration
    public final String register = hostname+"index.php?route=api/registration";


    public final String notification = hostname+"index.php?route=api/notification";


    //firebase store user instance_id // PARAM :  POST
    public final  String FIREBASE_SEND_TOKEN_URL = hostname+"index.php?route=api/firebase";


    //REMOVE FIREBASE INSTANCEID FROM SERVER : POST METHOD

    public final  String REMOVE_FIREBASE = hostname+"index.php?route=api/cloudmessage/deleteToken";




}
