package io.github.solid_kiss.peppyrus_api.model;

public class MessageBody {

  public static final String PROCESS_TYPE_DEFAULT = "cenbii-procid-ubl::urn:fdc:peppol.eu:2017:poacc:billing:01:1.0";
  public static final String DOCUMENT_TYPE_DEFAULT = "busdox-docid-qns::urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1";

  private String sender;

  private String recipient;

  private String processType = PROCESS_TYPE_DEFAULT;

  private String documentType = DOCUMENT_TYPE_DEFAULT;

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

}

