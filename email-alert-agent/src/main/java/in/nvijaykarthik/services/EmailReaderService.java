package in.nvijaykarthik.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.FlagTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import in.nvijaykarthik.config.EmailConfig;
import in.nvijaykarthik.model.EmailContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReaderService {

     @Autowired
     EmailConfig emailConfig;

    public List<EmailContent> readUnreadEmailsFromSender() {
        Properties properties = new Properties();
        properties.put("mail.imap.host", emailConfig.getHost());
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");
        List<EmailContent> emailContents = new ArrayList<>();

        try {
            Session session = Session.getInstance(properties);
            Store store = session.getStore("imap");
            store.connect(emailConfig.getUsername(), emailConfig.getPassword());

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY); // Change to READ_WRITE if you want to mark as read

            // Search for only UNREAD emails
            FlagTerm unreadFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message[] messages = inbox.search(unreadFlagTerm);

            for (Message message : messages)
             {
                Address[] froms = message.getFrom();
                if (froms != null && froms.length > 0) {
                    String fromEmail = ((InternetAddress) froms[0]).getAddress();
                    if (emailConfig.getAllowedFromAddress().contains(fromEmail.toLowerCase())) {
                        System.out.println("Unread Email from: " + fromEmail);
                        System.out.println("Subject: " + message.getSubject());
                        System.out.println("Received Date: " + message.getReceivedDate());
                        System.out.println("--------------------------------------------------");
                        // Optional: Mark email as read after processing
                        message.setFlag(Flags.Flag.SEEN, true);
                        EmailContent content=EmailContent.newContent(message);
                        emailContents.add(content);
                    }
                }
            }
            inbox.close(true);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailContents;
    }
}
