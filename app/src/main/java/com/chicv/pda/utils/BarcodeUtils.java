package com.chicv.pda.utils;

import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BarcodeUtils {


    /**
     * 通用二维码
     */
    public static final String REGEX_QR_CODE = "(?i)^(\\d{9})_(WP-\\d{9}|[0-9A-Z]+/\\d{9})_([A-Z0-9]+)$";

    /**
     * 捡货单号
     */
    public static final String REGEX_PICK_GOODS = "(?i)^JH-([?<num>\\d+]{9})$";
    /**
     * 内部拣货单号
     */
    public static final String REGEX_PICK_INTERNAL_GOODS = "(?i)^JHIN-([?<num>\\d+]{9})$";
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

    /**
     * 囤货发货批次
     */
    public static final String REGEX_BATCH_RULE = "(?i)^FH-([?<num>\\d+]{9})$";

    /**
     * 物流编号
     */
    public static final String REGEX_EXPRESS = "(?i)^([0-9a-zA-Z]{9,})$";

    /**
     * 盘货编号
     */
    public static final String REGEX_CHECK_GOODS = "(?i)^PH-([?<num>\\d+]{9})$";
    /**
     * 调拨单号
     */
    public static final String REGEX_TRANSFER_CODE = "(?i)^DB-([?<num>\\d+]{9})$";

    /**
     * 移库单号
     */
    public static final String REGEX_MOVE_CODE = "(?i)^MO-([?<num>\\d+]{9})$";

    /**
     * 移库单号
     */
    public static final String REGEX_NUM = "(?i)^(\\d+)$";


    public static boolean isPickCode(String username) {
        return Pattern.matches(REGEX_PICK_GOODS, username);
    }

    public static boolean isTransferCode(String username) {
        return Pattern.matches(REGEX_TRANSFER_CODE, username);
    }

    public static boolean isPickInternalCode(String username) {
        return Pattern.matches(REGEX_PICK_INTERNAL_GOODS, username);
    }

    public static boolean isStockCode(String username) {
        return Pattern.matches(REGEX_CONTAINER, username);
    }

    public static boolean isGoodsCode(String username) {
        return Pattern.matches(REGEX_GOODS, username);
    }

    public static boolean isGoodsRuleCode(String username) {
        return Pattern.matches(REGEX_GOODS_RULE, username);
    }

    public static boolean isBatchRule(String username) {
        return Pattern.matches(REGEX_BATCH_RULE, username);
    }

    public static boolean isExpressCode(String username) {
        return Pattern.matches(REGEX_EXPRESS, username);
    }

    public static boolean isMoveCode(String username) {
        return Pattern.matches(REGEX_MOVE_CODE, username);
    }

    public static boolean isQRCode(String username) {
        return Pattern.matches(REGEX_QR_CODE, username);
    }

    public static boolean isNum(String username) {
        return Pattern.matches(REGEX_NUM, username);
    }

    /**
     * 除去编号及0获取条码后面的数字ID
     *
     * @param code 条码号
     * @return
     */
    public static int getBarcodeId(String code) {
        int result = 0;
        if (!TextUtils.isEmpty(code)) {
            int i = code.indexOf("-");
            i++;
            if (i < code.length()) {
                try {
                    result = Integer.parseInt(code.substring(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 生成拣货条码 JH-000000001
     *
     * @param pickId 拣货单ID
     * @return
     */
    public static String generateJHBarcode(long pickId) {
        return String.format(Locale.CHINA, "JH-%09d", pickId);
    }

    /**
     * 生成内部拣货单条码 JHIN-000000001
     *
     * @param pickId 拣货单ID
     * @return
     */
    public static String generateJHINBarcode(long pickId) {
        return String.format(Locale.CHINA, "JHIN-%09d", pickId);
    }

    /**
     * 生成物品条码 WP-000000001
     *
     * @param pickId 拣货单ID
     * @return
     */
    public static String generateWPBarcode(long pickId) {
        return String.format(Locale.CHINA, "WP-%09d", pickId);
    }

    /**
     * 生成货位条码 WP-000000001
     *
     * @param stockId 货位ID
     * @return
     */
    public static String generateHWBarcode(long stockId) {
        return String.format(Locale.CHINA, "HW-%09d", stockId);
    }

    /**
     * 生成调拨条码 WP-000000001
     *
     * @param dbID 货位ID
     * @return
     */
    public static String generateDBBarcode(long dbID) {
        return String.format(Locale.CHINA, "DB-%09d", dbID);
    }

    /**
     * 生成条码
     *
     * @param id     id
     * @param prefix 条码前缀
     * @return 条码
     */

    public static String generateChicvBarcode(long id, String prefix) {
        return String.format(Locale.CHINA, prefix + "-%09d", id);
    }

    public static String getWpOrBatchCodeFromQr(String code) {
        String result = "";
        Pattern compile = Pattern.compile(REGEX_QR_CODE);
        Matcher matcher = compile.matcher(code);
        if (matcher.find()) {
            if (matcher.groupCount() > 2) {
                result = matcher.group(2);
            }
        }
        return result;
    }
}