package in.nvijaykarthik.services;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    @Autowired
    VectorStore vectorStore;

    public void storeTransaction(String failedMessage, String analysisDetails, String actionTaken) {
        Document doc = TransactionService.createDocument(failedMessage,  analysisDetails,  actionTaken);
        vectorStore.add(List.of(doc));
    }
}
