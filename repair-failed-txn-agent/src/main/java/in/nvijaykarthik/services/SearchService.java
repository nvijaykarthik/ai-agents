package in.nvijaykarthik.services;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    VectorStore vectorStore;
    @Autowired
    OllamaEmbeddingModel embModel;
 
    public List<Document> searchSimilarTxn(String query) {
        SearchRequest request=SearchRequest.builder().query(query).topK(5).similarityThreshold(0.7).build();
        return vectorStore.similaritySearch(request);
    }
}
