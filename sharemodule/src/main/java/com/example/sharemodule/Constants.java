package com.example.sharemodule;


public class Constants {

    public static final String EXTRA_LOCATION_DATA = "location_data";

    public interface GoogleApiClientConstant {

        // Google Api Client Constants
        int GOOGLE_API_CLIENT_TIMEOUT_S = 10; // 10 seconds
        String GOOGLE_API_CLIENT_ERROR_MSG =
                "Failed to connect to GoogleApiClient (error code = %d)";
        String CONNECTION_FAILED = "com.example.ram.connectionfialed";

    }


    public interface Keys {
        String CALL_LOGIN = "/call_login";
        String DATA_ITEM = "/data_item";
        String ACCOUNT_LIST_ITEM = "/account_list_item";
    }
}



