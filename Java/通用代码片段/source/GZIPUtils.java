/**
 *
 * 参考：<a href='https://blog.csdn.net/qq_37958845/article/details/119208909'>统一异常返回</a>
 */
public class GZIPUtils {

    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

    public static final int byteLength = 1024;

    /**
     * 字符串压缩为GZIP字节数组
     */
    public static byte[] compress(String str) {
        return compress(str, GZIP_ENCODE_UTF_8);
    }

    /**
     * 字符串压缩为GZIP字节数组
     */
    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            log.error("gzip compress error.", e);
        }
        return out.toByteArray();
    }

    /**
     * GZIP解压缩
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[byteLength];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            log.error("gzip uncompress error.", e);
        }
        return out.toByteArray();
    }

    /**
     * 解压
     */
    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }

    /**
     * 解压
     */
    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream unGzip = new GZIPInputStream(in);
            byte[] buffer = new byte[byteLength];
            int n;
            while ((n = unGzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            log.error("gzip uncompress to string error.", e);
        }
        return null;
    }
}