package hsbms.com.config;

import org.springframework.context.annotation.Configuration;
import org.opencv.core.Core;

@Configuration
public class OpenCVConfig {
    static {
        //nu.pattern.OpenCV.loadShared();
        //System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    	
        //System.loadLibrary("D:/2025_WORK/opencv/build/java/x64/opencv_java4120.dll");

    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
    }
}