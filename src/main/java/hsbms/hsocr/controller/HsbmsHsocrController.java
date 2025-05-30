package hsbms.hsocr.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hsbms.hsocr.util.OcrUtil;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HsbmsHsocrController {
	
	@GetMapping("/api/ocr")
    public String runOcr(@RequestParam String imagePath) {
        return OcrUtil.extractTextFromImage(imagePath);
    }
	
	
	//POST http://localhost:8080/api/ocr/upload
	@PostMapping("/api/ocr/upload")
    public String uploadAndOcr(@RequestParam("file") MultipartFile file) {
		
		//log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
        if (file.isEmpty()) {
            return "파일이 없습니다.";
        }

        try {
            // 임시 파일 생성
            File tempFile = File.createTempFile("ocr_", ".png");
            file.transferTo(tempFile);

            String ocrResult = OcrUtil.extractTextFromFile(tempFile);

            // 사용 후 삭제
            tempFile.delete();

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 종료");
            
            return ocrResult;


        } catch (IOException e) {
            return "파일 처리 실패: " + e.getMessage();
        }
    }

	@PostMapping("/api/ocr/uploaddata")
    public ResponseEntity<Map<String, String>> uploadImageData(@RequestBody Map<String, String> payload) {
        String imageData = payload.get("imageData");
        if (imageData != null && imageData.startsWith("data:image/png;base64,")) {
            String base64Image = imageData.substring("data:image/png;base64,".length());
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // 이미지 저장 (실제 서비스에서는 다른 방식으로 처리해야 할 수 있습니다.)
            String filePath = "uploaded_image_" + System.currentTimeMillis() + ".png";
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(imageBytes);
                System.out.println("이미지 저장 성공: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                Map<String, String> response = new HashMap<>();
                response.put("message", "이미지 저장 실패");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // 임시 파일 생성
            File tempFile = new File(filePath);
            // OCR 처리 로직 (추가 구현 필요)
            // ...
            String ocrResult = OcrUtil.extractTextFromFile(tempFile);

            // 사용 후 삭제
            //tempFile.delete();

            Map<String, String> response = new HashMap<>();
            response.put("message", "이미지 데이터 수신 및 처리 완료");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "유효하지 않은 이미지 데이터 형식");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
	
	
//	public class NameRegistry {
//	    private static final Set<String> registeredNames = new HashSet<>();
//
//	    public static boolean isRegistered(String name) {
//	        return registeredNames.contains(name);
//	    }
//
//	    public static void registerName(String name) {
//	        registeredNames.add(name);
//	        // TODO: DB에 저장하는 로직으로 대체 가능
//	    }
//	}
}
