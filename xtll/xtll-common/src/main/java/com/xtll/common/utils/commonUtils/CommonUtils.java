package com.xtll.common.utils.commonUtils;

public class CommonUtils {


    public static String[] getArrayByString(String str) {
        int num = 0;
        if ("0".equals(str)) {
            return new String[]{""};
        } else {
            if (str != null) {
                str.split(",");
            }
            return str.split(",");
        }
    }

    public static int[] getIntByString(String str) {
        int num = str.length() / 2 + 1;
        int[] xt = new int[num];
        for (int i = 0; i < xt.length; i++) {

            System.out.println(str.charAt(2*i));
            xt[i] = Integer.parseInt(str.charAt(2 * i)+"");
        }
        return xt;
    }
}
