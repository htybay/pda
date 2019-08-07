package com.chicv.pda;

import com.chicv.pda.bean.StockInfo;
import com.chicv.pda.bean.StockRecord;
import com.chicv.pda.utils.BarcodeUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void barcodeTest() {
        // 保留code的位数
//        String result = String.format("JH-%0" + 9 + "d", 7987879);
        String result = String.format("JH-%09d", 7987879);
        System.out.println(result);
    }

    @Test
    public void sortTest() {
        List<StockInfo> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            StockInfo stockInfo = new StockInfo();
            stockInfo.setAreaSort(i % 5);
            stockInfo.setShelfId(i % 4);
            stockInfo.setColumnId(i % 3);
            stockInfo.setRowsId(i % 2);
            list.add(stockInfo);
        }

        Collections.sort(list);
        for (StockInfo stockInfo : list) {
            System.out.println(stockInfo.getAreaSort() + "-" + stockInfo.getColumnId() + "-" + stockInfo.getShelfId() + "-" + stockInfo.getRowsId());
        }
    }

    @Test
    public void sortTest2() {
        List<StockRecord> list = new ArrayList<>();
        StockRecord record1 = new StockRecord();
        record1.setCreateTime("2019-01-24T09:29:25.063");
        StockRecord record2 = new StockRecord();
        record2.setCreateTime("2019-01-24T09:41:25.063");
        StockRecord record3 = new StockRecord();
        record3.setCreateTime("2019-01-24T09:28:25.063");
        list.add(record1);
        list.add(record2);
        list.add(record3);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getCreateTime());
        }

    }

    @Test
    public void wpRegTest() {
        String code = "000000000_WP-000000001_20190702123534515_YYB_TYTUY756756756_123456789";
        String code2 = "003732672_61DG/003732672_2019062412415709111";

        String reg = "(?i)^(WP-\\d{9}|[0-9A-Z]+/\\d{9})_(\\d{9})_([0-9A-Z]+)$";
        String reg2 = "(?i)^/(\\d{9})_((?:WP-\\d{9})|(?:[A-Za-z0-9]+/\\d{9}))_([A-Za-z0-9]+)/$";

        String reg3 = "(?i)^(\\d{9})_(WP-\\d{9}|[0-9A-Z]+/\\d{9})_([A-Z0-9]+)$";
        String reg4 = "(?i)^(\\d{9})_(WP-\\d{9}|[0-9A-Z]+/\\d{9})_([A-Z0-9]+)$";

        String reg5 = "(?i)^(\\d{9})_(WP-\\d{9}|[0-9A-Z]+/\\d{9})_([A-Z0-9]+)_(YYB_[0-9A-Z]+)_(\\d{9})$";


        System.out.println(Pattern.matches(reg, code));
        System.out.println(Pattern.matches(reg2, code));
        System.out.println(Pattern.matches(reg3, code));
        System.out.println(Pattern.matches(reg4, code));
        System.out.println(Pattern.matches(reg5, code));

        String barcode = code;
        if (BarcodeUtils.isQRCode(barcode)) {
            // 如果扫到的是二维码，提取出物品号或囤货规格
            barcode = BarcodeUtils.getWpOrBatchCodeFromQr(barcode);
        }
        System.out.println(barcode);


        Pattern compile = Pattern.compile(BarcodeUtils.REGEX_QR_CODE_NEW);
        Matcher matcher = compile.matcher(code);
        if (matcher.find()) {
            if (matcher.groupCount() > 2) {
                System.out.println(2 + ":" + matcher.group(2));
            }
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(i + ":" + matcher.group(i));
            }
        }
    }


}