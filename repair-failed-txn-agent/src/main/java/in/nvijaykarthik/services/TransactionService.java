package in.nvijaykarthik.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public static List<Document> createDocument(String failedMessage, String analysisDetails, String actionTaken) {
        // Store metadata (useful for filtering)
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("analysisDetails", analysisDetails);
        metadata.put("actionTaken", actionTaken);
        // Create the document (subject + body as content)
        TextSplitter splitter= TokenTextSplitter.builder().withChunkSize(512).build();
        List<Document> doc=splitter.split(new Document(failedMessage, metadata));
        return doc;
    }
    
}
