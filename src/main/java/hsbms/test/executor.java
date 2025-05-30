package hsbms.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class executor {
	public void sendorSdisk(SenddocSearchVO sendocSearchVO) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(new Runnable() {
        	@Override
        	public void run() {
        		log.debug("senddoc Start : senddocInvstProcess " );
        		senddocInvstProcess(sendocSearchVO);
        		boolean result1 = senddocPortal(sendocSearchVO);
        		log.debug("senddoc result1 : {} ", result1 );
        		if ( result1 ) {
        			boolean result2 = senddocProc(sendocSearchVO);
            		log.debug("senddoc result2 : {} ", result2 );
        		}
        	}
        });
	}
	public void senddocInvstProcess(SenddocSearchVO sendocSearchVO) {
		System.out.println("senddocInvstProcess");
		//Thread.sleep(3000);
	}
	public boolean senddocPortal(SenddocSearchVO sendocSearchVO) {
		boolean result = true;
		System.out.println("senddocPortal");
		return result;
	}
	public boolean senddocProc(SenddocSearchVO sendocSearchVO) {
		boolean result = true;
		System.out.println("senddocProc");
		return result;
	}

	/*
	 * 
	 * 
	 * 

	@Autowired
    private ExecutorServiceWrapper executorServiceWrapper;

    @GetMapping("/send")
    @ResponseBody
    public String handleSendRequest(@RequestParam String documentId) {
        SenddocSearchVO searchVO = new SenddocSearchVO(documentId);
        executorServiceWrapper.sendorSdisk(searchVO);
        return "Send request submitted for document ID: " + documentId + ". Processing in the background.";
    }
    
    	
	
	import org.springframework.transaction.annotation.Transactional;
	import org.springframework.stereotype.Service;
	
	@Service
	public class ExecutorService {
	
	    // ... (다른 코드)
	
	    @Transactional
	    public void integratedSenddocProcess(SenddocSearchVO sendocSearchVO) {
	        log.debug("senddoc Start : senddocInvstProcess ");
	        senddocInvstProcess(sendocSearchVO);
	        boolean result1 = senddocPortal(sendocSearchVO);
	        log.debug("senddoc result1 : {} ", result1 );
	        if ( result1 ) {
	            boolean result2 = senddocProc(sendocSearchVO);
	            log.debug("senddoc result2 : {} ", result2 );
	        }
	    }
	
	    public void sendorSdisk(SenddocSearchVO sendocSearchVO) {
	        ExecutorService executor = Executors.newFixedThreadPool(10);
	        executor.submit(() -> integratedSenddocProcess(sendocSearchVO));
	    }
	
	    public void senddocInvstProcess(SenddocSearchVO sendocSearchVO) {
	        System.out.println("senddocInvstProcess");
	        // ... (실제 로직)
	    }
	
	    public boolean senddocPortal(SenddocSearchVO sendocSearchVO) {
	        boolean result = true;
	        System.out.println("senddocPortal");
	        // ... (실제 로직)
	        return result;
	    }
	
	    public boolean senddocProc(SenddocSearchVO sendocSearchVO) {
	        boolean result = true;
	        System.out.println("senddocProc");
	        // ... (실제 로직)
	        return result;
	    }
	}
	 */
}
