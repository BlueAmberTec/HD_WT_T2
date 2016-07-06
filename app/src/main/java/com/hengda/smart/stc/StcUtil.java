package com.hengda.smart.stc;

/**
 * @author WZQ
 * @version V1.0
 * @Description ${todo}
 * @Email :wzq_steady@126.com
 * @date 2016/7/5 13:11
 * @update (date)
 */
public class StcUtil {

    public static byte getTixingByte(int i) {
        if (i == 1) {
            return (byte) 0x80;
        } else if (i == 2) {
            return (byte) 0x00;
        } else {
            return (byte) 0x00;
        }
    }
}
