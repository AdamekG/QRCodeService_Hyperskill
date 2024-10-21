package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.Map;

public class QRCodeGenerator {
    static QRCodeWriter writer = new QRCodeWriter();

    ErrorCorrectionLevel[] errorCorrectionLevels = ErrorCorrectionLevel.values();

    public QRCodeGenerator() {}

    public static BufferedImage createQRCode(String data, int size, ErrorCorrectionLevel corection) {

        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, corection);

        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            // handle the WriterException
            throw new RuntimeException(e.getMessage());
        }
    }
}
