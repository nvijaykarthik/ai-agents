package in.nvijaykarthik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import in.nvijaykarthik.config.EmailScheduler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class EmailAiAgentApp implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(EmailAiAgentApp.class, args);
  }

  @Autowired
  EmailScheduler emailScheduler;

  @Override
  public void run(String... args) throws Exception {
    emailScheduler.scheduleAnalyzeEmail();
  }


}
