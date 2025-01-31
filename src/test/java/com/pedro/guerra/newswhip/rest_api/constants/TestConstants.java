package com.pedro.guerra.newswhip.rest_api.constants;

public class TestConstants {
    public static final String REPORT_URL = "/report";
    public static final String ARTICLE_URL = "/article";

    public static final String COUNTRYCODE_IE = "ie";
    public static final String COUNTRYCODE_PT = "pt";
    public static final String COUNTRYCODE_ES = "es";
    public static final String COUNTRYCODE_INVALID = "ier";

    public static final String URL_IRISHTIMES = "www.irishtimes.com";
    public static final String URL_RTE = "www.rte.ie";
    public static final String URL_NEW = "https://www.sapo.pt/noticias/atualidade/trinta-eurodeputados-pedem-a-guterres-que_679c05093d9fcf74625368fc";
    public static final String URL_INVALID = "http/www.sapo.pt/noticias/atualidade/trinta-eurodeputados-pedem-a-guterres-que_679c05093d9fcf74625368fc";
    public static final String URL_EXISTING = "http://www.rte.ie/news/politics/2018/1004/1001034-cso/";
    
    public static final String BAD_REQUEST_MESSAGE_COUNTRYCODE = "Country Code needs to have only 2 letters";
    public static final String BAD_REQUEST_MESSAGE_INVALID_URL = "Url is not valid";
    public static final String BAD_REQUEST_MESSAGE_URL_NONEXISTENT = "There is no Article with that URL";
    public static final String BAD_REQUEST_MESSAGE_URL_ALREADY_EXISTENT = "There already exists an Article with that URL";
    public static final String BAD_REQUEST_MESSAGE_URL_BAD_SYNTAX = "One of the URLs under that domain doesn't have a correct Syntax";
}
