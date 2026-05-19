package com.codealpha.hotel_management_system.constant;

public class ApiConstant {
    private ApiConstant() {
    }
    // Base API path — every endpoint starts with this
    public static final String API = "/api/v1";
    public static final String SLASH = "/";

    public static final String AUTH = "auth";
    public static final String USERS = "users";
    public static final String HOTELS = "hotels";
    public static final String ROOMS = "rooms";
    public static final String RESERVATIONS = "reservations";
    public static final String PAYMENTS = "payments";

    // Action paths
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String LIST = "list";
    public static final String VIEW = "view";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String ME = "me";
    public static final String AVAILABLE = "available";
    public static final String STATUS = "status";
    public static final String CANCEL = "cancel";
    public static final String PAY = "pay";
    public static final String REFUND = "refund";
    public static final String SEARCH = "search";
    public static final String CITY = "city";
    public static final String EXPORT = "export";
}
