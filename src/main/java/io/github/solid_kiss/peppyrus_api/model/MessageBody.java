package io.github.solid_kiss.peppyrus_api.model;

public class MessageBody {

  public static final String PROCESS_TYPE_DEFAULT = "cenbii-procid-ubl::urn:fdc:peppol.eu:2017:poacc:billing:01:1.0";
  public static final String DOCUMENT_TYPE_INVOICE = "busdox-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1";
  public static final String DOCUMENT_TYPE_CREDIT_NOTE = "busdox-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1";

  private MessageBody() {}

  private String sender;

  private String recipient;

  private String processType = PROCESS_TYPE_DEFAULT;

  private String documentType = DOCUMENT_TYPE_INVOICE;

  private String fileContent;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getProcessType() {
    return processType;
  }

  public void setProcessType(String processType) {
    this.processType = processType;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public String getFileContent() {
    return fileContent;
  }

  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }

  public static MessageBody invoice(String sender, String recipient, String fileContent) {
    MessageBody messageBody = new MessageBody();
    messageBody.setDocumentType(DOCUMENT_TYPE_INVOICE);
    messageBody.setSender(sender);
    messageBody.setRecipient(recipient);
    messageBody.setFileContent(fileContent);
    messageBody.setProcessType(PROCESS_TYPE_DEFAULT);

    return messageBody;
  }

  public static MessageBody creditNote(String sender, String recipient, String fileContent) {
    MessageBody messageBody = new MessageBody();
    messageBody.setDocumentType(DOCUMENT_TYPE_CREDIT_NOTE);
    messageBody.setSender(sender);
    messageBody.setRecipient(recipient);
    messageBody.setFileContent(fileContent);
    messageBody.setProcessType(PROCESS_TYPE_DEFAULT);

    return messageBody;
  }

}

