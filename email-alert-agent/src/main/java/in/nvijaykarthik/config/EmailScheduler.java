package in.nvijaykarthik.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import in.nvijaykarthik.llm.AnalyseEmail;
import in.nvijaykarthik.model.EmailContent;
import in.nvijaykarthik.services.EmailReaderService;

@Service
public class EmailScheduler {

    @Autowired
    EmailReaderService emailReaderService;

    @Autowired
    AnalyseEmail analyseEmail;

    public void scheduleAnalyzeEmail() {

        // List<EmailContent>
        // emailContents=emailReaderService.readUnreadEmailsFromSender();
        List<EmailContent> emailContents = generateEmailContents();
        for (EmailContent emailContent : emailContents) {
            analyseEmail.analyseEmail(emailContent.getBody(), emailContent.getSubject());
        }
    }

    private List<EmailContent> generateEmailContents() {
        List<EmailContent> emailContents = new ArrayList<>();
        EmailContent emailContent = EmailContent.builder().body(sampleEmail).subject("Account Addition Failure Alert ")
                .build();
        emailContents.add(emailContent);
        return emailContents;
    }

    String sampleEmail = """

            Dear Gpoinath,

            You are esteemed customer , sorry for the incovenience caused.

            We hope this message finds you well. We were unable to successfully add a new account or user due to an issue on our end. Please find below the details of the failure and what steps we recommend taking
            next:

            1. **Review Account Details**: Double-check the account information provided during the addition process, including the account name, number, and other relevant details. Ensure they are accurate and match
            the account you intended to add.

            2. **Contact Support**: If the issue persists after verifying the account details, please reach out to our support team at [Support Email/Phone Number] for further assistance. They can help resolve the
            issue or provide additional troubleshooting steps.

            3. **Attempt Again Later**: If you need to attempt the account addition again later, ensure all required fields are correctly filled out and that your network settings allow remote access if needed.

            We apologize for the inconvenience caused and appreciate your patience as we work to resolve this matter. Please let us know if you have any further questions or concerns.

            Thank you for your understanding and cooperation.

            Best regards,
            rahul
            SR. Manager
            acme
            8787767654554
            """;

}
