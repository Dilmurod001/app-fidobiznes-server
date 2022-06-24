package com.dilmurod.utils;

public interface RestConstants {

    String HUMAN = "human";
    String HUMAN_COURSE = "human_course";
    String PAYMENT = "payment";
    String REGION = "region";
    String COURSE = "course";
    String ATTACHMENT = "attachment";
    String HOBBY = "hobby";
    String VIEW_OBJECT = "view_object";
    String VIEW_TYPES = "view_types";
    String VIEW_GROUP = "view_group";
    String VIEW_COLUMN = "view_column";
    String CUSTOM_FIELD = "custom_field";
    String CUSTOM_FIELD_VALUE = "custom_field_value";   //query un table nomi
    String CUSTOM_FIELD_DROP_DOWN = "custom_field_drop_down";//query un table nomi
    String CUSTOM_FIELD_LABEL = "custom_field_label";//query un table nomi
    String CUSTOM_FIELD_TREE = "custom_field_tree";
    String VIEW_FILTER = "view_filter";
    String VIEW_SORTING = "view_sorting";
    String FILTER_FIELD = "filter_field";
    String FILTER_FIELD_VALUE = "filter_field_value";

    String HUMAN_ADDITIONAL_NUMBER = "human_additional_number";

    String REQUEST_ATTRIBUTE_CURRENT_USER = "User";
    String REQUEST_ATTRIBUTE_CURRENT_USER_ID = "UserId";
    String REQUEST_ATTRIBUTE_CURRENT_USER_PERMISSIONS = "Permissions";

    String AUTHENTICATION_HEADER = "Authorization";


    /*REST API Error codes*/
    int INCORRECT_USERNAME_OR_PASSWORD = 3001;
    int EMAIL_OR_PHONE_EXIST = 3002;
    int EXPIRED = 3003;
    int ACCESS_DENIED = 3004;
    int NOT_FOUND = 3005;
    int INVALID = 3006;
    int REQUIRED = 3007;
    int SERVER_ERROR = 3008;
    int CONFLICT = 3009;
    int NO_ITEMS_FOUND = 3011;
    int CONFIRMATION = 3012;
    int USER_NOT_ACTIVE = 3013;
    int JWT_TOKEN_INVALID = 3014;


    /*
    ENTITY METHODS
     */
    String GET_ENTITY_FIELDS = "GET_ENTITY_FIELDS";
    String GET_ENTITY_NAME = "GET_ENTITY_NAME";


    /*
     OTHER SERVICE
    */
    String KETMON_SERVICE = "KETMON_SERVICE";


//    //DEFAULT VIEW LAR UCHUN UUID LAR YOZIB QO'YILDI DASRTUR HAR RUN BO'LGANIDA SHU VIEW LAR O'ZGARIB QO'YILADI
//    String humanTableViewId = "6d843798-9ac1-44af-8b9d-65232afb45e9";
//    String humanListViewId = "a7063ad3-e407-4409-83f4-d03927c0b8dd";
//    String humanBoardViewId = "84c81e9c-b03d-49ae-a6cb-85ecf554a6b4";


    //FUNKSIYA NI O'CHIRIB YANA YOZIB QO'YADI
    String query = "drop function if exists get_entity_id_list_for_generic_view;\n" +
            "create function get_entity_id_list_for_generic_view(sql_query character varying)\n" +
            "    returns TABLE\n" +
            "            (\n" +
            "                id_list    varchar\n" +
            "            )\n" +
            "    language plpgsql\n" +
            "as\n" +
            "$$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "        EXECUTE sql_query;\n" +
            "END\n" +
            "$$;\n" +
            "alter function get_entity_id_list_for_generic_view(varchar) owner to postgres;";

    String BASE_PATH_V1 = "/api/custom-field/v1";



}
