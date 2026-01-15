package be.solid_kiss.peppyrus_api.model;

public class OrganizationPeppolInfo {

  public static record Participant (String participantId, Boolean canReceive, Boolean isSender){}

  private Participant participants;

  public Participant getParticipants() {
    return participants;
  }

  public void setParticipants(Participant participants) {
    this.participants = participants;
  }
}
