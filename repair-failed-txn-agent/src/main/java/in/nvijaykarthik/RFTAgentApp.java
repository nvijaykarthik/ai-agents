package in.nvijaykarthik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import in.nvijaykarthik.llm.AnalyseTransaction;
import in.nvijaykarthik.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class RFTAgentApp implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(RFTAgentApp.class, args);
  }

  @Autowired
  AnalyseTransaction analyseTransaction;

  @Override
  public void run(String... args) throws Exception {
    String message = FileUtil.readFileToString("msg.xml");
    log.info(analyseTransaction.analyseTransaction(message));
  }

}
