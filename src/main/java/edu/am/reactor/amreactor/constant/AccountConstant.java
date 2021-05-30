package edu.am.reactor.amreactor.constant;

public enum AccountConstant {

    GET_ALL_ACCOUNTS("/accounts/v1/get"),
    GET_ACCOUNT_BY_ID("/accounts/v1/get/"),
    UPDATE_ACCOUNT("/accounts/v1/update");

    private String uri;

    AccountConstant(String resourceUri) {
        this.uri = resourceUri;
    }

    public String getUrl() {
        return uri;
    }
}
