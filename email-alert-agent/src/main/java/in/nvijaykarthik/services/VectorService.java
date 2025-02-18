package in.nvijaykarthik.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VectorService {

    @Autowired
    VectorStore vectorStore;

    public List<Document> createDocument(String failedMessage, String analysisDetails, String actionTaken) {
        // Store metadata (useful for filtering)
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("analysisDetails", analysisDetails);
        metadata.put("actionTaken", actionTaken);
        TextSplitter splitter= TokenTextSplitter.builder().withChunkSize(512).build();
        List<Document> docs=splitter.split(new Document(failedMessage, metadata));
        // Create the document (subject + body as content)
        return docs;
    }
    
     public void storeTransaction(String failedMessage, String analysisDetails, String actionTaken) {
        List<Document> docs= createDocument(failedMessage,  analysisDetails,  actionTaken);
        vectorStore.add(docs);
    }
    
    public List<Document> searchSimilarEmail(String query) {
        SearchRequest request=SearchRequest.builder().query(query).topK(5).similarityThreshold(0.7).build();
        return vectorStore.similaritySearch(request);
    }
}
