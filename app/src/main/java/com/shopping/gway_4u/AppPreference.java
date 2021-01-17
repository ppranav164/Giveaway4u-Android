package com.shopping.gway_4u;


public class AppPreference {

    private String dummyAmount = "10";//"10";
    private String dummyEmail = "";//"";//d.basak.db@gmail.com
    private String productInfo ="";// "product_info";
    private String firstName = ""; //"firstname";
    private boolean isOverrideResultScreen = true;
 
    public static final String USER_EMAIL = "";
    public static final String USER_MOBILE = "";
    public static final String PHONE_PATTERN = "^[987]\\d{9}$";
    public static final long MENU_DELAY = 300;
    public static String USER_DETAILS = "user_details";
    public static int selectedTheme = -1;

    private boolean isDisableWallet, isDisableSavedCards, isDisableNetBanking, isDisableThirdPartyWallets, isDisableExitConfirmation;

    boolean isDisableWallet() {
        return isDisableWallet;
    }

    void setDisableWallet(boolean disableWallet) {
        isDisableWallet = disableWallet;
    }

    boolean isDisableSavedCards() {
        return isDisableSavedCards;
    }

    void setDisableSavedCards(boolean disableSavedCards) {
        isDisableSavedCards = disableSavedCards;
    }

    boolean isDisableNetBanking() {
        return isDisableNetBanking;
    }

    void setDisableNetBanking(boolean disableNetBanking) {
        isDisableNetBanking = disableNetBanking;
    }
    boolean isDisableThirdPartyWallets() {
        return isDisableThirdPartyWallets;
    }

    void setDisableThirdPartyWallets(boolean disableThirdPartyWallets) {
        isDisableThirdPartyWallets = disableThirdPartyWallets;
    }
    boolean isDisableExitConfirmation() {
        return isDisableExitConfirmation;
    }

    void setDisableExitConfirmation(boolean disableExitConfirmation) {
        isDisableExitConfirmation = disableExitConfirmation;
    }

    public String getDummyAmount() {
        return dummyAmount;
    }

    public void setDummyAmount(String dummyAmount) {
        this.dummyAmount = dummyAmount;
    }

    public String getDummyEmail() {
        return dummyEmail;
    }

    public void setDummyEmail(String dummyEmail) {
        this.dummyEmail = dummyEmail;
    }

    public boolean isOverrideResultScreen() {
        return isOverrideResultScreen;
    }

    public void setOverrideResultScreen(boolean overrideResultScreen) {
        isOverrideResultScreen = overrideResultScreen;
    }
    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
