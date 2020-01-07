package com.ianplunkett.productsearch.Util;

/*
MoneyConverter is a crude regex matcher that strips dollar signs,
commas and periods from string so that money can be converted into
integer values for indexing purposes.
 */
public class MoneyConverter {
    public static int dollarsStringToInteger(String price) {
        return Integer.parseInt(price.replaceAll("[^\\d]+", ""));
    }
}
