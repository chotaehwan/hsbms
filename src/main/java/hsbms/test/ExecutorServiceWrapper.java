package hsbms.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class ExecutorServiceWrapper {

    private static final Logger log = LoggerFactory.getLogger(ExecutorServiceWrapper.class);
    private ExecutorService executor;
    private final int threadPoolSize = 10; // 스레드 풀 크기 설정

    @PostConstruct
    public void initializeExecutor() {
        executor = Executors.newFixedThreadPool(threadPoolSize);
        log.info("ExecutorService initialized with {} threads.", threadPoolSize);
    }

    public void sendorSdisk(SenddocSearchVO sendocSearchVO) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                log.debug("senddoc Start : senddocInvstProcess for {}", sendocSearchVO.getId());
                try {
                    boolean invstResult = senddocInvstProcess(sendocSearchVO);
                    log.debug("senddocInvstProcess result for {} : {}", sendocSearchVO.getId(), invstResult);
                    if (invstResult) {
                        boolean portalResult = senddocPortal(sendocSearchVO);
                        log.debug("senddocPortal result for {} : {}", sendocSearchVO.getId(), portalResult);
                        if (portalResult) {
                            boolean procResult = senddocProc(sendocSearchVO);
                            log.debug("senddocProc result for {} : {}", sendocSearchVO.getId(), procResult);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error processing senddoc for {}", sendocSearchVO.getId(), e);
                    // 필요한 오류 처리 로직 (예: 알림, 상태 업데이트 등)
                } finally {
                    log.debug("senddoc processing finished for {}", sendocSearchVO.getId());
                }
            }
        });
    }

    public boolean senddocInvstProcess(SenddocSearchVO sendocSearchVO) {
        log.info("senddocInvstProcess started for {}", sendocSearchVO.getId());
        try {
            // 파일 이동 및 네트워크 통신 등의 무거운 작업 수행
            Thread.sleep(5000); // 예시: 5초 동안 작업 수행
            log.info("senddocInvstProcess finished successfully for {}", sendocSearchVO.getId());
            return true; // 성공
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("senddocInvstProcess interrupted for {}", sendocSearchVO.getId(), e);
            return false; // 실패
        } catch (Exception e) {
            log.error("Error in senddocInvstProcess for {}", sendocSearchVO.getId(), e);
            return false; // 실패
        }
    }

    public boolean senddocPortal(SenddocSearchVO sendocSearchVO) {
        log.info("senddocPortal started for {}", sendocSearchVO.getId());
        boolean result = true;
        // senddocPortal 관련 로직
        log.info("senddocPortal finished for {} with result: {}", sendocSearchVO.getId(), result);
        return result;
    }

    public boolean senddocProc(SenddocSearchVO sendocSearchVO) {
        log.info("senddocProc started for {}", sendocSearchVO.getId());
        boolean result = true;
        // senddocProc 관련 로직
        log.info("senddocProc finished for {} with result: {}", sendocSearchVO.getId(), result);
        return result;
    }

    @PreDestroy
    public void destroyExecutor() {
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
                log.info("ExecutorService shutdown successfully.");
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
                log.error("Error during ExecutorService shutdown.", e);
            }
        }
    }
}

