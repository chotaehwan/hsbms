package hsbms.hsocr.service;

import net.sourceforge.tess4j.Tesseract;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class HsbmsHsocrService {
	/*
	 * 
		OCR 정제 순서 요약
		1. Grayscale
		2. Thresholding (Binary)
		3. Denoising (Blur, Morphology)
		4. Resize (if small)
		5. Deskew (Skew correction)
		6. ROI 자르기
		7. OCR 적용
	*/
	
	//OCR 이미지 처리
    public String extractTextFromImage(MultipartFile file) {
        try {
            // UUID로 결과 폴더 생성
            String uuid = UUID.randomUUID().toString();
            Path outputDir = Path.of("ocr-output", uuid);
            Files.createDirectories(outputDir);

            // 1단계: 파일 -> Mat
            Mat original = Imgcodecs.imdecode(new MatOfByte(file.getBytes()), Imgcodecs.IMREAD_COLOR);
            Imgcodecs.imwrite(outputDir.resolve("step1_original.png").toString(), original);

            // 2단계: Grayscale
            Mat gray = toGrayScale(original);
            Imgcodecs.imwrite(outputDir.resolve("step2_gray.png").toString(), gray);

            // 3단계: Binary
            Mat binary = toBinary(gray);
            Imgcodecs.imwrite(outputDir.resolve("step3_binary.png").toString(), binary);

            // 4단계: Denoise
            Mat denoised = denoise(binary);
            Imgcodecs.imwrite(outputDir.resolve("step4_denoised.png").toString(), denoised);

            // 5단계: Resize
            Mat resized = resize(denoised);
            Imgcodecs.imwrite(outputDir.resolve("step5_resized.png").toString(), resized);

            // OCR
            BufferedImage image = matToBufferedImage(resized);
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("tessdata");
            tesseract.setLanguage("eng+kor");
            String result = tesseract.doOCR(image);

            // 결과 저장
            Files.writeString(outputDir.resolve("result.txt"), result);

            return "처리 완료: ID = " + uuid;

        } catch (Exception e) {
            return "OCR 처리 중 오류: " + e.getMessage();
        }
    }
    
    private Mat toGrayScale(Mat input) {
        Mat gray = new Mat();
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }

    private Mat toBinary(Mat gray) {
        Mat binary = new Mat();
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        return binary;
    }

    private Mat denoise(Mat binary) {
        Mat denoised = new Mat();
        Imgproc.medianBlur(binary, denoised, 3);
        return denoised;
    }

    private Mat resize(Mat input) {
        Mat resized = new Mat();
        Imgproc.resize(input, resized, new Size(input.width() * 2, input.height() * 2));
        return resized;
    }

    private BufferedImage matToBufferedImage(Mat mat) throws Exception {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".png", mat, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }
    
	
}
