package in.nvijaykarthik.llm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import in.nvijaykarthik.services.SearchService;
import in.nvijaykarthik.services.StoreService;
import in.nvijaykarthik.util.FileUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalyseTransaction {


    @Autowired
    SearchService searchService;

    @Autowired
    StoreService storeService;

    @Autowired
    OllamaChatModel llm;

    @Autowired
    VectorStore vectorStore;

    String prompt;

    @PostConstruct
    public void init() {
        prompt=FileUtil.readFileToString("txnprompt.txt");
        log.info("Prompt loaded {}",prompt);
    }

    public String analyseTransaction(String failedMessage) {
        
        List<Document> similarTxnDocs = searchService.searchSimilarTxn(failedMessage);
        log.info("Found {} similar transactions",similarTxnDocs.size());
        // Format similar emails into a readable string
        StringBuilder similarTxns = new StringBuilder();
        for (Document doc : similarTxnDocs) {
            similarTxns.append("- ").append(doc.getFormattedContent()).append("\n");
        }
        String formattedPrompt=prompt.replace("${CONTENT}",failedMessage).replace("${SIMILAR_SEARCH}",similarTxns.toString());
        log.info("Prompting user with {}",formattedPrompt);
        UserMessage message = new UserMessage(formattedPrompt);
        String analysisResult = llm.call(message);
        String action = "No action taken";
        storeService.storeTransaction(failedMessage, analysisResult, action);
        return analysisResult;
    }

    



}
