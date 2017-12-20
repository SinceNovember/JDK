package com.liu;

import java.lang.annotation.Native;
public final class Integer extends Number implements Comparable<java.lang.Integer> {
    //值为 （－（2的31次方）） 的常量，它表示 int 类型能够表示的最小值。
    @Native public static final int   MIN_VALUE = 0x80000000;
    //值为 （（2的31次方）－1） 的常量，它表示 int 类型能够表示的最大值。
    @Native public static final int   MAX_VALUE = 0x7fffffff;
    @SuppressWarnings("unchecked")
    //表示基本类型 int 的 Class 实例。
    public static final Class<java.lang.Integer>  TYPE = (Class<java.lang.Integer>) Class.getPrimitiveClass("int");
    final static char[] digits = {    //用于转换为字符串的数组。
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    };
    public static String toString(int i, int radix) {//radix为需要变成的进制
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            radix = 10; //如果不在32进制范围内，则改为10进制。

        /* Use the faster version */
        if (radix == 10) {//基数为10，则采用快速版本toString
            return toString(i);
        }

        char buf[] = new char[33];
        boolean negative = (i < 0);//判断数是否正还是负
        int charPos = 32;//从最后一位设起

        if (!negative) {//如果为负数，转化为正数。
            i = -i;
        }
        while (i <= -radix) { //栈，计算数字在不同进制下的表示方式              假设i=-16   redix=10进制
            buf[charPos--] = digits[-(i % radix)]; //得到第i个字符，来表示数字   则buf存入-(-16%10)=6
            i = i / radix;                                                       // i=-16/10=-6;
        }
        buf[charPos] = digits[-i];//第一位的数字的字符存入buf                     将`6`存入

        if (negative) {   //判断符号 在前面添加`-`符号用
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, (33 - charPos));//charPos开始的下标，33-charPos是有效数字的长度
    }
    public static String toUnsignedString(int i, int radix) {//无符号字符串
        return Long.toUnsignedString(toUnsignedLong(i), radix);
    }
    public static String toHexString(int i) {//无符号十六进制字符串
        return toUnsignedString0(i, 4);
    }
    public static String toOctalString(int i) {//无符号八进制字符串
        return toUnsignedString0(i, 3);
    }
    public static String toBinaryString(int i) {//无符号二进制字符串
        return toUnsignedString0(i, 1);
    }
    private static String toUnsignedString0(int val, int shift) {
        // assert shift > 0 && shift <=5 : "Illegal shift value";
        int mag = SIZE - numberOfLeadingZeros(val);
        int chars = Math.max(((mag + (shift - 1)) / shift), 1);
        char[] buf = new char[chars];

        formatUnsignedInt(val, shift, buf, 0, chars);

        // Use special constructor which takes over "buf".
        return new String(buf, true);
    }
    static int formatUnsignedInt(int val, int shift, char[] buf, int offset, int len) {//用于进行进制转换
        int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] = digits[val & mask];
            val >>>= shift;
        } while (val != 0 && charPos > 0);

        return charPos;
    }

    final static char [] DigitTens = {//十位上的数字数组
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
            '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
            '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
            '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
            '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
            '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
            '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
            '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
    } ;

    final static char [] DigitOnes = {//个位上的数字数组
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    } ;

    public static String toString(int i) {//返回i的字符串型
        if (i == MIN_VALUE)//越界，返回最小字符串
            return "-2147483648";
        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);//根据i返回字符数组的长度
        char[] buf = new char[size];
        getChars(i, size, buf);//获取字符
        return new String(buf, true);//将字符数组变为数组返回
    }
    public static String toUnsignedString(int i) {
        return Long.toString(toUnsignedLong(i));
    }
    static void getChars(int i, int index, char[] buf) {//获取对应 的字符数组
        int q, r;
        int charPos = index;//数组大小
        char sign = 0;//标志位

        if (i < 0) {//如果小于0设置标志位为'-',并取反
            sign = '-';
            i = -i;
        }

        // 处理超过2的16次方的大数.
        while (i >= 65536) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = i - ((q << 6) + (q << 5) + (q << 2));//相当于r=i-(q*100)
            i = q;   // i=i/10;
            buf [--charPos] = DigitOnes[r];//个位上的数字
            buf [--charPos] = DigitTens[r];//十位上的数字
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i <= 65536, i);
        for (;;) {
            q = (i * 52429) >>> (16+3);//无符号右移,等于q=i/10的效果.
            r = i - ((q << 3) + (q << 1));  // q左移3加左移1,等于r=i-q*10的效果，r等于除10余数
            buf [--charPos] = digits [r];//找到相应的字符到着存入buf数组
            i = q;//i=i/10;
            if (i == 0) break;
        }
        if (sign != 0) {//如果为负数，则在前面加上一个`-`;
            buf [--charPos] = sign;
        }
    }

    final static int [] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, MAX_VALUE };

    // Requires positive x
    static int stringSize(int x) {//根据int返回字符数组的大小
        for (int i=0; ; i++)
            if (x <= sizeTable[i])//返回的是10的倍数
                return i+1;

    }
    public static int parseInt(String s, int radix)//String解析成Int型
            throws NumberFormatException
    {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        //进制 必须 大于等于 2 小于等于 36
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix +
                    " less than Character.MIN_RADIX");
        }
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix +
                    " greater than Character.MAX_RADIX");
        }
        int result = 0;
        boolean negative = false;//是否负数
        int i = 0, len = s.length();
        //负数的话 limit 取值 Integer.MIN_VALUE :-2147483648
        //正数取值 -Integer.MAX_VALUE :-2147483647
        int limit = -MAX_VALUE;
        int multmin;
        int digit;
        if (len > 0) {
            //取第一个字符,判断是否负数
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {//判断第一位是否为负数
                    negative = true;
                    limit = MIN_VALUE;
                } else if (firstChar != '+')
                    throw NumberFormatException.forInputString(s);

                if (len == 1) // Cannot have lone "+" or "-"
                    throw NumberFormatException.forInputString(s);
                i++; //自增,指向第二个字符
            }
            // 限制的最大值 / 进制 , 是解析结果没有 * 进制前 限制,最大的值,超过则说明解析字符串 超过int的最大范围(正数负数limit不同)
            multmin = limit / radix;
            while (i < len) {
                //字符根据进制获取数值,不能转换的返回-1
                digit = Character.digit(s.charAt(i++),radix);
                if (digit < 0) {
                    throw NumberFormatException.forInputString(s);
                }
                if (result < multmin) {
                    throw NumberFormatException.forInputString(s);
                }
                result *= radix;//乘以进制后 校验, result(负数) < limit(负数) + digit(这次解析到的数字,正数)  则超出int范围
                if (result < limit + digit) {
                    throw NumberFormatException.forInputString(s);
                }
                result -= digit; //result 赋值为 负的 字符解析的数值
            }
        } else {
            throw NumberFormatException.forInputString(s);
        }
        return negative ? result : -result;//result 的到的都是负数,进行加符号返回
    }
    public static int parseInt(String s) throws NumberFormatException {//默认10进制进行字符串解析成int
        return parseInt(s,10);
    }
    public static int parseUnsignedInt(String s, int radix)//无符号字符换int 如果为负数的话，抛出异常.
            throws NumberFormatException {
        if (s == null)  {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new
                        NumberFormatException(String.format("Illegal leading minus sign " +
                        "on unsigned string %s.", s));
            } else {
                if (len <= 5 || // Integer.MAX_VALUE in Character.MAX_RADIX is 6 digits
                        (radix == 10 && len <= 9) ) { // Integer.MAX_VALUE in base 10 is 10 digits
                    return parseInt(s, radix);
                } else {
                    long ell = Long.parseLong(s, radix);
                    if ((ell & 0xffff_ffff_0000_0000L) == 0) {
                        return (int) ell;
                    } else {
                        throw new
                                NumberFormatException(String.format("String value %s exceeds " +
                                "range of unsigned int.", s));
                    }
                }
            }
        } else {
            throw NumberFormatException.forInputString(s);
        }
    }
    public static int parseUnsignedInt(String s) throws NumberFormatException {
        return parseUnsignedInt(s, 10);
    }
    public static java.lang.Integer valueOf(String s, int radix) throws NumberFormatException {//转换为Integer类
        return valueOf(parseInt(s,radix));
    }

    public static java.lang.Integer valueOf(String s) throws NumberFormatException {
        return valueOf(parseInt(s, 10));
    }

    private static class IntegerCache {//用于缓存的静态内部类
        static final int low = -128;//存放 -128 - 127(默认)之间的缓存Integer对象
        static final int high;
        static final java.lang.Integer cache[];

        static {//静态初始化
            // high value may be configured by property
            int h = 127;//默认最大127
            //配置参数IntegerCache.high可修改
            String integerCacheHighPropValue =
                    sun.misc.VM.getSavedProperty("IntegerCache.high");  //配置参数IntegerCache.high可修改
            if (integerCacheHighPropValue != null) {
                try {
                    //参数解析成int
                    int i = parseInt(integerCacheHighPropValue);
                    i = Math.max(i, 127);
                    //最大值范围 Integer.MAX_VALUE - 128 - 1 ;
                    h = Math.min(i, MAX_VALUE - (-low) -1);
                } catch( NumberFormatException nfe) {
                    // If the property cannot be parsed into an int, ignore it.
                }
            }
            high = h;
                //创建cache,并对cache进行初始化
            cache = new java.lang.Integer[(high - low) + 1];
            int j = low;
            for(int k = 0; k < cache.length; k++)
                cache[k] = new java.lang.Integer(j++);//对缓存数组初始化,为每个数组全创建为一个Integer对象

            assert IntegerCache.high >= 127;
        }

        private IntegerCache() {}  //私有构造函数,不能类外实例化,这个内部类不需要实例,只需要静态的存储数组cache
    }
    public static java.lang.Integer valueOf(int i) {//int型转integer
        if (i >= IntegerCache.low && i <= IntegerCache.high)//判断i的值是否在缓存[-128~127]范围内,如果在，则返回缓存的Integer.
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new java.lang.Integer(i);//超过范围，创建一个新的Integer对象.
    }

    private final int value;
    public Integer(int value) {//构造函数
        this.value = value;
    }
    public Integer(String s) throws NumberFormatException {//字符串构造函数
        this.value = parseInt(s, 10);//调用解析成int函数
    }
    public byte byteValue() {//返回字节型
        return (byte)value;
    }

    public short shortValue() {//短型值
        return (short)value;
    }
    public int intValue() {//返回int值
        return value;
    }
    public long longValue() {//返回长整形
        return (long)value;
    }
    public float floatValue() {//返回浮点型
        return (float)value;
    }
    public double doubleValue() {//返回double型
        return (double)value;
    }
    public String toString() {//返回字符串型。
        return toString(value);
    }
    @Override
    public int hashCode() {
        return hashCode(value);
    }
    public static int hashCode(int value) {
        return value;
    }
    public boolean equals(Object obj) {//判断对象是否相等
        if (obj instanceof java.lang.Integer) {
            return value == ((java.lang.Integer)obj).intValue();
        }
        return false;
    }
    public static java.lang.Integer getInteger(String nm) {//获取int值
        return getInteger(nm, null);
    }
    public static java.lang.Integer getInteger(String nm, int val) {
        java.lang.Integer result = getInteger(nm, null);
        return (result == null) ? valueOf(val) : result;
    }
    public static java.lang.Integer getInteger(String nm, java.lang.Integer val) {
        String v = null;
        try {
            v = System.getProperty(nm);
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        if (v != null) {
            try {
                return decode(v);
            } catch (NumberFormatException e) {
            }
        }
        return val;
    }
    //这个方法是解析16进制 8进制字符串的.
//以0x 0X # 开头的是16进制
//以 0 开头(并且长度大于1)的是 8 进制
//其他是10进制
    public static java.lang.Integer decode(String nm) throws NumberFormatException {
        int radix = 10;
        int index = 0;
        boolean negative = false;
        java.lang.Integer result;

        if (nm.length() == 0)
            throw new NumberFormatException("Zero length string");
        //判断正负
        char firstChar = nm.charAt(0);
        // Handle sign, if present
        if (firstChar == '-') {
            negative = true;
            index++;
        } else if (firstChar == '+')
            index++;

        // Handle radix specifier, if present
        if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {//如果开头为0x或0X的,则为16进制。
            index += 2;
            radix = 16;
        }
        else if (nm.startsWith("#", index)) {//#开头也为16进制
            index ++;
            radix = 16;
        }
        else if (nm.startsWith("0", index) && nm.length() > 1 + index) {//以0开头的为8进制
            index ++;
            radix = 8;
        }

        if (nm.startsWith("-", index) || nm.startsWith("+", index))
            throw new NumberFormatException("Sign character in wrong position");

        try {
            result = valueOf(nm.substring(index), radix);//截取符号和进制标志位后的数字
            result = negative ? valueOf(-result.intValue()) : result;//添加符号
        } catch (NumberFormatException e) {
            // If number is Integer.MIN_VALUE, we'll end up here. The next line
            // handles this case, and causes any genuine format error to be
            // rethrown.
            //再重新解析一次,负数加上符号位,排除Integer.MIN_VALUE的错误
            String constant = negative ? ("-" + nm.substring(index))
                    : nm.substring(index);
            result = valueOf(constant, radix);
        }
        return result;
    }
    public int compareTo(java.lang.Integer anotherInteger) {
        return compare(this.value, anotherInteger.value);
    }
    public static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
    public static int compareUnsigned(int x, int y) {
        return compare(x + MIN_VALUE, y + MIN_VALUE);
    }
    public static long toUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
    }
    public static int divideUnsigned(int dividend, int divisor) {
        return (int)(toUnsignedLong(dividend) / toUnsignedLong(divisor));
    }
    public static int remainderUnsigned(int dividend, int divisor) {
        return (int)(toUnsignedLong(dividend) % toUnsignedLong(divisor));
    }
    @Native public static final int SIZE = 32;
    public static final int BYTES = SIZE / Byte.SIZE;
    /**
     * 方法用途 - 取一个二进制数最先出现1的 位保留1 其余位都变成 0
     * 比如 0100_1111 -> 0100_0000 , 保留最先出现 1的位,其余变 0 返回最高位为1, 其它位为0的数
     * 计算方式 :比如一个二进制最高位第 n位  ,最低位为第1 位 :
     * n , n|n-1 , n|n-1|n-2 , n|n-1|n-2|n-3, ... , n|n-1|...|2|1
     * 那么最先出现 1 的位与其后的位 都是 1
     * i - i 无符号向右移 1 位  得到最先出现 1的最高位为1 ,其余位都是 0
     * (如  0111_1111 - 0011_1111 = 0100_0000)
     */
    public static int highestOneBit(int i) {
        // HD, Figure 3-1
        i |= (i >>  1);
        i |= (i >>  2);
        i |= (i >>  4);
        i |= (i >>  8);
        i |= (i >> 16);
        return i - (i >>> 1);
    }
    //方法功能-得到最低位出现 1的其余位为0 的数
    public static int lowestOneBit(int i) {
        // HD, Section 2-1
        //计算方式 : i补码 & i -> i的反码+ 1 & i
        //因为 i反码+1 使最低位出现1 的变成 1,更低位的都是0,  i补码 & i得到正确的数
        return i & -i;
    }
    //方法功能 - 取最高位为 1 前面 0 位的数量
//二分查找进行计算,不断缩小范围
    public static int numberOfLeadingZeros(int i) {
        // HD, Figure 5-6
        if (i == 0)
            return 32;
        int n = 1;
        if (i >>> 16 == 0) { n += 16; i <<= 16; }
        if (i >>> 24 == 0) { n +=  8; i <<=  8; }
        if (i >>> 28 == 0) { n +=  4; i <<=  4; }
        if (i >>> 30 == 0) { n +=  2; i <<=  2; }
        n -= i >>> 31;
        return n;
    }
    //方法说明 - 取最低位是1 后面的 0 位的个数,二分查找法
    public static int numberOfTrailingZeros(int i) {
        // HD, Figure 5-14
        int y;
        if (i == 0) return 32;
        int n = 31;
        y = i <<16; if (y != 0) { n = n -16; i = y; }
        y = i << 8; if (y != 0) { n = n - 8; i = y; }
        y = i << 4; if (y != 0) { n = n - 4; i = y; }
        y = i << 2; if (y != 0) { n = n - 2; i = y; }
        return n - ((i << 1) >>> 31);
    }
    //计算 - 2进制是 1 的位数
//二分法 先计算每两位中的1的个数 , 再计算4位中1的个数 .. 32位中1的个数
    public static int bitCount(int i) {
        // HD, Figure 5-2
        i = i - ((i >>> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        i = (i + (i >>> 4)) & 0x0f0f0f0f;
        i = i + (i >>> 8);
        i = i + (i >>> 16);
        return i & 0x3f;
    }
    //方法功能 - 循环左移 distance 位
//注意 移位运算32位 只有distance数字的低五位有效,64位数字 低6位有效
//所以 右移-distance位可以表示成 右移32 - distance位.
    public static int rotateLeft(int i, int distance) {
        return (i << distance) | (i >>> -distance);
    }
    //方法功能 - 循环右移 distance 位
    public static int rotateRight(int i, int distance) {
        return (i >>> distance) | (i << -distance);
    }
    //方法功能 - 翻转一个二进制数(32位,最低位变成最高位,第二 低位变成第二高位),同bitCount计算方式一样, 先翻转每两位的顺序,再翻转每4位中的顺序,再翻转每8位中的顺序,让后的到32位翻转后顺序
    public static int reverse(int i) {
        // HD, Figure 7-1
        i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;
        i = (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333;
        i = (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f;
        i = (i << 24) | ((i & 0xff00) << 8) |
                ((i >>> 8) & 0xff00) | (i >>> 24);
        return i;
    }
    public static int signum(int i) {
        // HD, Section 2-7
        return (i >> 31) | (-i >>> 31);
    }
    public static int reverseBytes(int i) {
        return ((i >>> 24)           ) |
                ((i >>   8) &   0xFF00) |
                ((i <<   8) & 0xFF0000) |
                ((i << 24));
    }
    public static int sum(int a, int b) {
        return a + b;
    }
    public static int max(int a, int b) {
        return Math.max(a, b);
    }
    public static int min(int a, int b) {
        return Math.min(a, b);
    }
    @Native private static final long serialVersionUID = 1360826667806852920L;
}

