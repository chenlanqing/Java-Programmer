import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 竖屏图片检查（宽高比接近9:16或0.562）
 */
public class ImageSizeChecker {

    /**
     * 检查图片是否为竖屏图片（宽高比接近9:16或0.562）
     *
     * @param imageUrl 图片URL地址
     * @return true表示是竖屏图片，false表示不是
     */
    public static boolean isPortraitImage(String imageUrl) {
        ImageDimension dimension = getImageDimension(imageUrl);
        double ratio = dimension.getAspectRatio();

        // 检查是否接近0.562或9:16（约0.5625）的比例
        // 使用较大的误差容忍度（5%）来同时支持这两种情况
        double targetRatio = 0.562; // 或者使用9.0/16 ≈ 0.5625
        double tolerance = 0.05; // 5%的误差容忍度

        return Math.abs(ratio - targetRatio) <= (targetRatio * tolerance);
    }

    /**
     * 检查图片URL的宽高比是否为9:16（允许一定误差）
     *
     * @param imageUrl 图片URL地址
     * @return true表示宽高比为9:16（或接近），false表示不是
     */
    public static boolean isAspectRatio9To16(String imageUrl) {
        return checkAspectRatio(imageUrl, 9, 16, 0.05); // 使用5%的误差容忍度
    }

    /**
     * 检查图片URL的宽高比是否符合指定比例
     *
     * @param imageUrl    图片URL地址
     * @param widthRatio  宽度比例
     * @param heightRatio 高度比例
     * @return true表示宽高比符合指定比例，false表示不符合
     */
    public static boolean checkAspectRatio(String imageUrl, int widthRatio, int heightRatio) {
        return checkAspectRatio(imageUrl, widthRatio, heightRatio, 0.05); // 默认误差为5%
    }

    /**
     * 检查图片URL的宽高比是否符合指定比例（带误差容忍）
     *
     * @param imageUrl    图片URL地址
     * @param widthRatio  宽度比例
     * @param heightRatio 高度比例
     * @param tolerance   误差容忍度（0.05表示5%的误差）
     * @return true表示宽高比符合指定比例，false表示不符合
     */
    public static boolean checkAspectRatio(String imageUrl, int widthRatio, int heightRatio, double tolerance) {
        if (StringUtils.isBlank(imageUrl)) {
            throw new IllegalArgumentException("图片URL不能为空");
        }
        if (widthRatio <= 0 || heightRatio <= 0) {
            throw new IllegalArgumentException("宽高比例必须大于0");
        }
        if (tolerance < 0) {
            throw new IllegalArgumentException("误差容忍度不能为负数");
        }
        // 获取图片尺寸
        ImageDimension dimension = getImageDimension(imageUrl);

        // 计算实际宽高比
        double actualRatio = (double) dimension.width / dimension.height;

        // 计算期望宽高比
        double expectedRatio = (double) widthRatio / heightRatio;

        // 计算误差
        double difference = Math.abs(actualRatio - expectedRatio);
        double allowedDifference = expectedRatio * tolerance;

        return difference <= allowedDifference;
    }

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static ImageDimension getImageDimension(String image) {
        if (image.startsWith("http")) {
            return getImageDimensionFromUrl(image);
        } else {
            return getImageDimensionFromFile(image);
        }
    }

    /**
     * 获取网络图片的尺寸信息
     *
     * @param imageUrl 图片URL地址
     * @return 图片尺寸信息
     */
    public static ImageDimension getImageDimensionFromUrl(String imageUrl) {
        Request request = new Request.Builder()
                .url(imageUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP请求失败，响应码: " + response.code());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("响应体为空");
            }

            String contentType = response.header("Content-Type");
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IOException("URL指向的不是图片文件，Content-Type: " + contentType);
            }

            try (InputStream inputStream = body.byteStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                return new ImageDimension(image);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ImageDimension getImageDimensionFromFile(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            BufferedImage image = ImageIO.read(inputStream);
            return new ImageDimension(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 图片尺寸信息类
     */
    public static class ImageDimension {
        public final int width;
        public final int height;

        public ImageDimension(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public ImageDimension(BufferedImage image) {
            if (image == null) {
                throw new RuntimeException("无法解析图片，可能不是有效的图片文件");
            }
            this.width = image.getWidth();
            this.height = image.getHeight();
        }

        /**
         * 获取宽高比
         *
         * @return 宽高比
         */
        public double getAspectRatio() {
            return (double) width / height;
        }

        /**
         * 获取宽高比字符串表示
         *
         * @return 宽高比字符串，如"16:9"
         */
        public String getAspectRatioString() {
            int gcd = gcd(width, height);
            return (width / gcd) + ":" + (height / gcd);
        }

        /**
         * 计算最大公约数
         */
        private int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }

        @Override
        public String toString() {
            return String.format("ImageDimension{width=%d, height=%d, aspectRatio=%.3f, ratioString='%s'}", width, height, getAspectRatio(), getAspectRatioString());
        }
    }
}