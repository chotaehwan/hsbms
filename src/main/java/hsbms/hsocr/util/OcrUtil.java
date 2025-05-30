package hsbms.hsocr.util;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OcrUtil {

    private static final String TESSDATA_PATH = "C:/Program Files/Tesseract-OCR/tessdata"; // 시스템에 맞게 변경
    private static final String LANGUAGE = "kor"; // 또는 "eng", 필요에 따라 설정

    public static String extractTextFromImage(String imagePath) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSDATA_PATH);
        tesseract.setLanguage(LANGUAGE);

        try {
            File imageFile = new File(imagePath);
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            return "OCR 실패: " + e.getMessage();
        }
    }
    
    public static String extractTextFromFile(File imageFile) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSDATA_PATH);
        tesseract.setLanguage(LANGUAGE);
        System.out.println("extractTextFromFile STTTTTTTT");
        
        try {
            String result = tesseract.doOCR(imageFile);
            String[] lines = result.split("\\r?\\n");
            for (String line : lines) {
                System.out.println("라인: " + line);
            }
            System.out.println("extractTextFromFile 나오난 ");
        } catch (TesseractException e) {
            return "OCR 실패: " + e.getMessage();
        }
        
        System.out.println("extractTextFromFile EMD");
        return "OK";
    }
    
}
/*
 콘텐츠로 건너뛰기
채팅 기록



오늘
지난 7일
지난 30일
3월
2월
1월
2024년
나의 말:
tesseract.doOCR 리턴 값 활용 방법을 알려주세요.
ChatGPT의 말:
tesseract.doOCR(...) 메서드는 Java에서 Tesseract OCR 라이브러리를 사용할 때 OCR 결과를 문자열 형태로 반환해줍니다. 이 문자열은 일반적으로 이미지에서 추출된 텍스트 전체를 포함하며, 줄바꿈(\n)을 포함한 형식으로 되어 있습니다.

예를 들어 다음과 같이 사용할 수 있습니다:

File imageFile = new File("example.png");
Tesseract tesseract = new Tesseract();
tesseract.setDatapath("tessdata"); // tessdata 폴더 경로 지정
tesseract.setLanguage("eng"); // 사용할 언어 설정

try {
    String result = tesseract.doOCR(imageFile);
    System.out.println(result); // 결과 출력
} catch (TesseractException e) {
    e.printStackTrace();
}
리턴 값 String result 활용 방법
이 result 문자열을 다양한 방식으로 가공하거나 활용할 수 있습니다:

1. 라인별로 분리해서 사용
String[] lines = result.split("\\r?\\n");
for (String line : lines) {
    System.out.println("라인: " + line);
}
2. 특정 패턴 추출 (예: 이름, 숫자, 날짜 등)
정규 표현식을 활용해 원하는 정보를 추출할 수 있습니다.

Pattern namePattern = Pattern.compile("[가-힣]{2,4}"); // 한글 이름 패턴
Matcher matcher = namePattern.matcher(result);
while (matcher.find()) {
    System.out.println("이름 후보: " + matcher.group());
}
3. 단어별로 분리
공백 기준으로 단어 분리:

String[] words = result.split("\\s+");
for (String word : words) {
    System.out.println("단어: " + word);
}
4. 서버에 전송하기 전 JSON 포맷으로 변환
import org.json.JSONObject;

JSONObject json = new JSONObject();
json.put("ocrResult", result);
// 이후 HTTP 요청으로 서버에 전송
5. 후처리: 오타 정정, 불필요한 문자 제거
예: 특수문자 제거, 공백 정리 등

String cleaned = result.replaceAll("[^가-힣a-zA-Z0-9\\s]", "").trim();
필요한 결과가 "이름"만 추출하는 것이라면, 예시로 아래와 같이 필터링도 가능합니다:

String[] lines = result.split("\\r?\\n");
for (String line : lines) {
    if (line.matches(".*[가-힣]{2,4}.*")) { // 간단한 이름 조건
        System.out.println("이름 후보: " + line);
    }
}
혹시 doOCR 결과에서 특정 항목(예: 이름, 금액)을 자동으로 판별해야 하는 경우, 해당 항목 포맷을 알려주시면 맞춤 예제도 드릴 수 있어요.






검색

이성



ChatGPT는 실수를 할 수 있습니다. 중요한 정보는 재차 확인하세요.
?
*/
 
