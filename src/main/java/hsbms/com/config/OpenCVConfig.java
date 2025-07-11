package hsbms.com.config;

import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;

import javax.annotation.PostConstruct;

import org.opencv.core.Core;
import org.slf4j.LoggerFactory;

@Configuration
public class OpenCVConfig {
    private final static Logger log = (Logger) LoggerFactory.getLogger("OpenCVConfig");

    @PostConstruct
    public void init() {
        System.out.println("OpenCV native library loaded via @PostConstruct");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV native library loaded via @PostConstruct");
    }
    /*
    static {
        //nu.pattern.OpenCV.loadShared();
        //System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        //System.loadLibrary("D:/2025_WORK/opencv/build/java/x64/opencv_java4120.dll");
    	//System.out.println("OpenCV native lib loaded: " + Core.NATIVE_LIBRARY_NAME);
    	log.info("OpenCV native lib loaded: " + Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    */
}