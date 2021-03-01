package io.yzecho.netdisk.utils;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author yzecho
 * @desc
 * @date 13/01/2021 14:30
 */
public class QrCodeUtil {

    /**
     * 编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 二维码格式
     */
    private static final String FORMAT = "JPG";

    /**
     * 二维码尺寸
     */
    private static final Integer QRCODE_SIZE = 300;

    /**
     * 二维码宽度
     */
    private static final Integer LOGO_WIDTH = 60;

    /**
     * 二维码高度
     */
    private static final Integer LOGO_HEIGHT = 60;

    /**
     * 生成二维码
     *
     * @param content
     * @param logoPath
     * @param compress
     * @return
     */
    public static BufferedImage createImage(String content, String logoPath, boolean compress) throws WriterException {
        Map<EncodeHintType, Object> hintMap = Maps.newHashMap();
        // 纠错级别
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hintMap.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hintMap.put(EncodeHintType.MARGIN, 1);

        // 创建比特矩阵（位矩阵）的QR码编码的字符串
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hintMap);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoPath == null || "".equals(logoPath)) {
            return image;
        }
        QrCodeUtil.insertImage(image, logoPath, compress);
        return image;
    }

    /**
     * 插入logo
     *
     * @param source
     * @param logoPath
     * @param compress
     */
    private static void insertImage(BufferedImage source, String logoPath, boolean compress) {
        InputStream inputStream = null;
        try {
            inputStream = QrCodeUtil.class.getResourceAsStream(logoPath);
            Image src = ImageIO.read(inputStream);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            if (compress) {
                // 压缩logo
                width = width > LOGO_WIDTH ? LOGO_WIDTH : width;
                height = height > LOGO_HEIGHT ? LOGO_HEIGHT : height;
                Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(image, 0, 0, null);
                // 绘制缩小后的图
                g.dispose();
                src = image;
            }
            // 插入logo
            Graphics2D graphics = source.createGraphics();
            int x = (QRCODE_SIZE - width) / 2;
            int y = (QRCODE_SIZE - height) / 2;
            graphics.drawImage(src, x, y, width, height, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graphics.setStroke(new BasicStroke(3f));
            graphics.draw(shape);
            graphics.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 生成二维码，获得到输出流，logo内嵌
     *
     * @param content
     * @param logoPath
     * @param output
     * @param compress
     */
    public static void encode(String content, String logoPath, OutputStream output, boolean compress) throws WriterException, IOException {
        BufferedImage image = QrCodeUtil.createImage(content, logoPath, compress);
        ImageIO.write(image, FORMAT, output);
    }
}
