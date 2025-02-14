package in.nvijaykarthik.model;

import jakarta.mail.Message;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailContent {
    String subject;
    String body;
    String from;
    String to;
    String cc;
    String bcc;
    String receivedDate;
    String sentDate;
    String attachments;
    String importance;
    String priority;
    String read;
    String flagged;
    String size;
    String folder;
    String messageID;
    String conversationID;
    String categories;
    String sensitivity;
    String header;
    String bodyType;
    String bodyTypeDescription;
    String bodySize;
    String bodyPreview;
    String internetMessageID;
    String isReadReceiptRequested;
    String isDeliveryReceiptRequested;
    String isDraft;
    String isRead;

    public static EmailContent newContent(Message message){
        try {
            return EmailContent.builder()
                    .subject(message.getSubject())
                    .body(message.getContent().toString())
                    .from(message.getFrom()[0].toString())
                    .to(message.getRecipients(Message.RecipientType.TO) != null ? message.getRecipients(Message.RecipientType.TO)[0].toString() : null)
                    .cc(message.getRecipients(Message.RecipientType.CC) != null ? message.getRecipients(Message.RecipientType.CC)[0].toString() : null)
                    .bcc(message.getRecipients(Message.RecipientType.BCC) != null ? message.getRecipients(Message.RecipientType.BCC)[0].toString() : null)
                    .receivedDate(message.getReceivedDate() != null ? message.getReceivedDate().toString() : null)
                    .sentDate(message.getSentDate() != null ? message.getSentDate().toString() : null)
                    .size(String.valueOf(message.getSize()))
                    .messageID(message.getHeader("Message-ID") != null ? message.getHeader("Message-ID")[0] : null)
                    .bodySize(String.valueOf(message.getSize()))
                    .internetMessageID(message.getHeader("Message-ID") != null ? message.getHeader("Message-ID")[0] : null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
