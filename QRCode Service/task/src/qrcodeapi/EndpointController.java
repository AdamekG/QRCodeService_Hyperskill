package qrcodeapi;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

@RestController
class EndpointController {
    @GetMapping("/api/health")
    public ResponseEntity<String> getServiceHealth() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getQRCode(
            @RequestParam String contents,
            @RequestParam(defaultValue = "250") int size,
            @RequestParam(defaultValue = "png") String type,
            @RequestParam(defaultValue = "L") String correction) {

        if (contents == null ||contents.isBlank()) {
            ErrorResponse errorResponse = new ErrorResponse("Contents cannot be null or blank");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (size < 150 || size > 350) {
            ErrorResponse errorResponse = new ErrorResponse("Image size must be between 150 and 350 pixels");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!correction.matches("[LHMQ]")) {
            ErrorResponse errorResponse = new ErrorResponse("Permitted error correction levels are L, M, Q, H");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        BufferedImage qrcode = QRCodeGenerator.createQRCode(contents, size, ErrorCorrectionLevel.valueOf(correction));

        if ((type.equals("gif")) || type.equals("GIF")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_GIF)
                    .body(qrcode);
        }

        if (type.equals("png") || type.equals("PNG")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrcode);
        }

        if (type.equals("jpeg") || type.equals("JPEG")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(qrcode);
        }

        ErrorResponse response = new ErrorResponse("Only png, jpeg and gif image types are supported");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
