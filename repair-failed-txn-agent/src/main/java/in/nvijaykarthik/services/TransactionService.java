package in.nvijaykarthik.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public static Document createDocument(String failedMessage, String analysisDetails, String actionTaken) {
        // Store metadata (useful for filtering)
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("analysisDetails", analysisDetails);
        metadata.put("actionTaken", actionTaken);


        // Create the document (subject + body as content)
        return new Document(failedMessage, metadata);
    }
    
}
