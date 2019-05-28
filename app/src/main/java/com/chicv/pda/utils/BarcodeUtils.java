package com.chicv.pda.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class BarcodeUtils {

    /**
     * 捡货单号
     */
    public static final String REGEX_PICK_GOODS = "(?i)^JH-([?<num>\\d+]{9})$";
    /**
     * 货位编号
     */
    public static final String REGEX_CONTAINER = "(?i)^HW-([?<num>\\d+]{9})$";
    /**
     * 物品编号
     */
    public static final String REGEX_GOODS = "(?i)^WP-([?<num>\\d+]{9})$";

    /**
     * 囤货规格
     */
    public static final String REGEX_GOODS_RULE = "(?i)^[0-9A-Z]+/([?<num>\\d+]{9})$";


    public static boolean isPickCode(String username) {
        return Pattern.matches(REGEX_PICK_GOODS, username);
    }

    public static boolean isContainerCode(String username) {
        return Pattern.matches(REGEX_CONTAINER, username);
    }

    public static boolean isGoodsCode(String username) {
        return Pattern.matches(REGEX_GOODS, username);
    }

    public static boolean isGoodsRuleCode(String username) {
        return Pattern.matches(REGEX_GOODS_RULE, username);
    }

    /**
     * 除去编号及0获取条码后面的数字ID
     *
     * @param code 条码号
     * @return
     */
    public static long getBarcodeId(String code) {
        long result = 0;
        if (!TextUtils.isEmpty(code)) {
            int i = code.indexOf("-");
            i++;
            if (i < code.length()) {
                try {
                    result = Long.parseLong(code.substring(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
