package com.chicv.pda;

import com.chicv.pda.bean.StockInfo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String,Object> map = new HashMap<>();
       System.out.println(map.values().size());
    }


}