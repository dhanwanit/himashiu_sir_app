package com.arcticfoxappz.walmartcoupon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by ntf-19 on 25/1/18.
 */
//https://sheets.googleapis.com/v4/spreadsheets/1dOdRQcBZcLk19RL90L6_ogZKA4Sp2VXjv3TO3im_57A/values/Sheet1!A2:C?majorDimension=ROWS&fields=values&key=AIzaSyDIH7NOysm2C7Vvl3sjIwBSHiFa8pI1Pvs

public class AppConstants {
    private static final String key = "AIzaSyDIH7NOysm2C7Vvl3sjIwBSHiFa8pI1Pvs";
    //"AIzaSyAU0e9_jAboXOmysKfCaRqhdTMvpPNWlGs";

    private static final String SpreadSheetId_more_apps = "1bmvXTiHFrtQxcv7yZDxB9k2CjvI4K9P0vkFZxfPtKY8";
    //"1OIPiirFRLWk8sTH-OQ6vEr1OXiL8_mZ2iKzQANCwXFw";

    private static final String category_moreapps = "Sheet1";
    public static final String fullURL_moreapps = "https://sheets.googleapis.com/v4/spreadsheets/"
            +SpreadSheetId_more_apps + "/values/" + category_moreapps
            + "!A2:C?majorDimension=ROWS&fields=values&key="+key;
    private static final String SpreadSheetId_coupons = "1dOdRQcBZcLk19RL90L6_ogZKA4Sp2VXjv3TO3im_57A";
    private static final String category_coupons = "Sheet1";
    public static final String fullURL_coupons = "https://sheets.googleapis.com/v4/spreadsheets/14m6i8H7w-CVB7VIHQ4uADmikjJad_BLSgvmjLU4GQls/values/Walmart?key=AIzaSyBDCnj7XzJwSTWX3ex0dZj16jaf8O5terU\n";
    //public static final String fullURL_coupons = "https://sheets.googleapis.com/v4/spreadsheets/1GhzOoJvaAaUsZ2vI7lPORc-_tSjMoktHSm2QkkT9Tok/values/Sheet1?key=AIzaSyBDCnj7XzJwSTWX3ex0dZj16jaf8O5terU";
    public static final String check_version = "https://sheets.googleapis.com/v4/spreadsheets/1GhzOoJvaAaUsZ2vI7lPORc-_tSjMoktHSm2QkkT9Tok/values/Sheet2?key=AIzaSyBDCnj7XzJwSTWX3ex0dZj16jaf8O5terU";

    private static final String rating_SheetId = "1LLrLHJ1XpkbeVES9rRUNlNwvIu3ERKWnDFKL6z_Vf6k";
    private static final String rating_category = "Sheet2";
    public static final String rating_url="https://sheets.googleapis.com/v4/spreadsheets/" + SpreadSheetId_more_apps + "/values/" + rating_category + "!A2:C?majorDimension=ROWS&fields=values&key="+key;

    public static boolean bool_more_app_for_resume;

    public static String app_id_link="https://play.google.com/store/apps/details?id=com.arcticfoxappz.walmartcoupon";
    public static String developer_id_link="https://play.google.com/store/apps/developer?id=Readfox";

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            System.out.println("Internet Connection Not Present");
            return false;
        }
    }
}
