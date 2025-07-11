package hsbms.hsocr.util;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class OpenCVImagePreprocessor {

    /**
     * 이미지를 읽어 Mat 객체로 로드합니다.
     *
     * @param imagePath 읽어올 이미지 파일의 경로
     * @return 로드된 이미지의 Mat 객체, 실패 시 null 반환
     */
    public Mat loadImage(String imagePath) {
        return Imgcodecs.imread(imagePath);
    }

    /**
     * 이미지의 크기를 지정된 너비와 높이로 조정합니다.
     *
     * @param image 원본 이미지 Mat 객체
     * @param width 조정할 너비
     * @param height 조정할 높이
     * @return 크기가 조정된 이미지의 Mat 객체
     */
    public Mat resizeImage(Mat image, int width, int height) {
        Mat resizedImage = new Mat();
        Size size = new Size(width, height);
        Imgproc.resize(image, resizedImage, size);
        return resizedImage;
    }

    /**
     * 컬러 이미지를 그레이스케일 이미지로 변환합니다.
     *
     * @param image 원본 컬러 이미지 Mat 객체
     * @return 그레이스케일 이미지의 Mat 객체
     */
    public Mat convertToGrayscale(Mat image) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        return grayImage;
    }

    /**
     * 그레이스케일 이미지를 이진화합니다 (otsu's thresholding 사용).
     *
     * @param grayImage 원본 그레이스케일 이미지 Mat 객체
     * @return 이진화된 이미지의 Mat 객체
     */
    public Mat binarizeImage(Mat grayImage) {
        Mat binaryImage = new Mat();
        Imgproc.threshold(grayImage, binaryImage, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        return binaryImage;
    }

    /**
     * 이미지의 노이즈를 제거하기 위해 가우시안 블러링을 적용합니다.
     *
     * @param image 원본 이미지 Mat 객체
     * @param kernelSize 가우시안 커널의 크기 (홀수, 예: new Size(5, 5))
     * @param sigmaX X 방향 표준 편차
     * @return 노이즈가 제거된 이미지의 Mat 객체
     */
    public Mat applyGaussianBlur(Mat image, Size kernelSize, double sigmaX) {
        Mat blurredImage = new Mat();
        Imgproc.GaussianBlur(image, blurredImage, kernelSize, sigmaX);
        return blurredImage;
    }

    /**
     * 이미지에 침식 연산을 적용하여 작은 흰색 노이즈를 제거합니다.
     *
     * @param binaryImage 이진화된 이미지 Mat 객체
     * @param kernelSize 침식 연산에 사용할 커널 크기 (예: new Size(3, 3))
     * @return 침식 연산이 적용된 이미지의 Mat 객체
     */
    public Mat applyErosion(Mat binaryImage, Size kernelSize) {
        Mat erodedImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kernelSize);
        Imgproc.erode(binaryImage, erodedImage, kernel);
        return erodedImage;
    }

    /**
     * 이미지에 팽창 연산을 적용하여 끊어진 문자를 연결하거나 흰색 영역을 확장합니다.
     *
     * @param binaryImage 이진화된 이미지 Mat 객체
     * @param kernelSize 팽창 연산에 사용할 커널 크기 (예: new Size(3, 3))
     * @return 팽창 연산이 적용된 이미지의 Mat 객체
     */
    public Mat applyDilation(Mat binaryImage, Size kernelSize) {
        Mat dilatedImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kernelSize);
        Imgproc.dilate(binaryImage, dilatedImage, kernel);
        return dilatedImage;
    }

    /**
     * 열림 연산 (침식 후 팽창)을 적용하여 작은 흰색 점 노이즈를 제거합니다.
     *
     * @param binaryImage 이진화된 이미지 Mat 객체
     * @param kernelSize 연산에 사용할 커널 크기 (예: new Size(3, 3))
     * @return 열림 연산이 적용된 이미지의 Mat 객체
     */
    public Mat applyOpening(Mat binaryImage, Size kernelSize) {
        Mat openedImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kernelSize);
        Imgproc.morphologyEx(binaryImage, openedImage, Imgproc.MORPH_OPEN, kernel);
        return openedImage;
    }

    /**
     * 닫힘 연산 (팽창 후 침식)을 적용하여 문자 내부의 작은 검은색 점 노이즈를 채웁니다.
     *
     * @param binaryImage 이진화된 이미지 Mat 객체
     * @param kernelSize 연산에 사용할 커널 크기 (예: new Size(3, 3))
     * @return 닫힘 연산이 적용된 이미지의 Mat 객체
     */
    public Mat applyClosing(Mat binaryImage, Size kernelSize) {
        Mat closedImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, kernelSize);
        Imgproc.morphologyEx(binaryImage, closedImage, Imgproc.MORPH_CLOSE, kernel);
        return closedImage;
    }

    /**
     * 이미지의 기울기를 보정합니다 (허프 선 변환 사용).
     * **주의:** 이 방법은 이미지에 뚜렷한 직선이 존재할 때 효과적이며, 모든 경우에 완벽하게 작동하지 않을 수 있습니다.
     *
     * @param binaryImage 이진화된 이미지 Mat 객체
     * @return 기울기가 보정된 이미지의 Mat 객체
     */
    public Mat deskewImage(Mat binaryImage) {
        Mat dest = new Mat();
        Mat lines = new Mat();
        Imgproc.HoughLinesP(binaryImage, lines, 1, Math.PI / 180, 100, 100, 10);
        double angle = 0;
        if (lines != null) {
            for (int i = 0; i < lines.rows(); i++) {
                double[] vec = lines.get(i, 0);
                double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
                angle += Math.atan2(y2 - y1, x2 - x1);
            }
            if (lines.rows() > 0) {
                angle /= lines.rows();
            }
        }
        double degrees = Math.toDegrees(angle);
        Point center = new Point(binaryImage.cols() / 2, binaryImage.rows() / 2);
        Mat rotMat = Imgproc.getRotationMatrix2D(center, degrees, 1);
        Imgproc.warpAffine(binaryImage, dest, rotMat, binaryImage.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));
        return dest;
    }

    /**
     * 이미지에서 텍스트 영역의 윤곽선을 검출합니다.
     *
     * @param binaryImage 이진화된 이미지 Mat 객체
     * @return 검출된 윤곽선 목록 (List<MatOfPoint>)
     */
    public List<MatOfPoint> findTextContours(Mat binaryImage) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }

    /**
     * 검출된 윤곽선을 기반으로 텍스트 영역을 추출합니다.
     *
     * @param originalImage 원본 이미지 Mat 객체
     * @param contours      검출된 윤곽선 목록
     * @return 추출된 텍스트 영역 이미지 목록 (List<Mat>)
     */
    public List<Mat> extractTextRegions(Mat originalImage, List<MatOfPoint> contours) {
        List<Mat> textRegions = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            Mat roi = new Mat(originalImage, rect);
            textRegions.add(roi);
        }
        return textRegions;
    }

    // 필요에 따라 추가적인 전처리 메소드를 구현할 수 있습니다.

    /**
     * 정의된 모든 전처리 단계를 순서대로 적용합니다.
     *
     * @param imagePath 원본 이미지 파일 경로
     * @param resizeWidth 조정할 너비 (0이면 조정 안 함)
     * @param resizeHeight 조정할 높이 (0이면 조정 안 함)
     * @param gaussianKernelSize 가우시안 커널 크기 (null이면 적용 안 함)
     * @param erosionKernelSize 침식 커널 크기 (null이면 적용 안 함)
     * @param dilationKernelSize 팽창 커널 크기 (null이면 적용 안 함)
     * @param applyDeskew 기울기 보정 적용 여부
     * @return 전처리가 완료된 이미지의 Mat 객체
     */
    public Mat preprocessImage(String imagePath, int resizeWidth, int resizeHeight,
                               Size gaussianKernelSize, Size erosionKernelSize, Size dilationKernelSize,
                               boolean applyDeskew) {
        Mat image = loadImage(imagePath);
        if (image == null) {
            System.err.println("이미지 로드 실패: " + imagePath);
            return null;
        }

        if (resizeWidth > 0 && resizeHeight > 0) {
            image = resizeImage(image, resizeWidth, resizeHeight);
        }

        Mat grayImage = convertToGrayscale(image);
        Mat binaryImage = binarizeImage(grayImage);

        if (gaussianKernelSize != null) {
            binaryImage = applyGaussianBlur(binaryImage, gaussianKernelSize, 0);
        }

        if (erosionKernelSize != null) {
            binaryImage = applyErosion(binaryImage, erosionKernelSize);
        }

        if (dilationKernelSize != null) {
            binaryImage = applyDilation(binaryImage, dilationKernelSize);
        }

        if (applyDeskew) {
            binaryImage = deskewImage(binaryImage);
        }

        return binaryImage;
    }

    public static void main(String[] args) {
        // OpenCV 네이티브 라이브러리 로드 (환경에 맞게 경로 설정)
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        OpenCVImagePreprocessor preprocessor = new OpenCVImagePreprocessor();
        //String imagePath = "path/to/your/image.png"; // 실제 이미지 파일 경로로 변경
        String imagePath = "D:/2025_WORK/uploaded_image_1745623583496.png";
        // 전처리 파라미터 설정 (필요에 따라 조절)
        int resizeWidth = 600;
        int resizeHeight = 0; // 원본 비율 유지
        Size gaussianKernelSize = new Size(5, 5);
        Size erosionKernelSize = new Size(2, 2);
        Size dilationKernelSize = new Size(2, 2);
        boolean applyDeskew = true;

        Mat processedImage = preprocessor.preprocessImage(imagePath, resizeWidth, resizeHeight,
                gaussianKernelSize, erosionKernelSize, dilationKernelSize, applyDeskew);

        if (processedImage != null) {
            //Imgcodecs.imwrite("path/to/save/processed_image.png", processedImage); // 처리된 이미지 저장 경로 및 파일명 변경
            Imgcodecs.imwrite("D:/2025_WORK/processed_image.png", processedImage); // 처리된 이미지 저장 경로 및 파일명 변경
            System.out.println("이미지 전처리 완료 및 저장.");
        }
    }
}
