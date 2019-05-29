package com.chicv.pda;

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void barcodeTest(){
        // 保留code的位数
//        String result = String.format("JH-%0" + 9 + "d", 7987879);
        String result = String.format("JH-%09d", 7987879);
        System.out.println(result);
    }
}