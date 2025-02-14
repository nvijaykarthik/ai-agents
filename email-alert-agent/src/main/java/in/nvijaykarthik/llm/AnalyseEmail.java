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

import com.fasterxml.jackson.databind.util.JSONPObject;

import in.nvijaykarthik.services.VectorService;
import in.nvijaykarthik.util.FileUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalyseEmail {


    @Autowired
    VectorService storeService;

    @Autowired
    OllamaChatModel llm;

    @Autowired
    VectorStore vectorStore;

    String prompt;

    @PostConstruct
    public void init() {
        prompt=FileUtil.readFileToString("emailAnalysisPrompt.txt");
        log.info("Prompt loaded {}",prompt);
    }

    public String analyseEmail(String subject, String body) {
        
        List<Document> similarTxnDocs = storeService.searchSimilarEmail("Subject: "+subject+"Body: "+body);
        log.info("Found {} similar emails",similarTxnDocs.size());
        // Format similar emails into a readable string
        StringBuilder similarTxns = new StringBuilder();
        for (Document doc : similarTxnDocs) {
            similarTxns.append("- ").append(doc.getFormattedContent()).append("\n");
        }
        String formattedPrompt=prompt.replace("${SUBJECT}",subject).replace("${BODY}",body).replace("${SIMILAR_SEARCH}",similarTxns.toString());
        log.info("Prompting user with {}",formattedPrompt);
        UserMessage message = new UserMessage(formattedPrompt);
        String analysisResult = llm.call(message);
        log.info("Analysis result: {}",analysisResult);

        String action = "No action taken";


        storeService.storeTransaction("Subject: "+subject+"Body: "+body, analysisResult, action);
        return analysisResult;
    }
}
