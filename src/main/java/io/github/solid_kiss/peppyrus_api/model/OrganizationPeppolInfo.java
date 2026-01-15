package io.github.solid_kiss.peppyrus_api.model;

public class OrganizationPeppolInfo {

  /**
   * @param participantId
   * @param canReceive
   * @param isSender
   */
  public static record Participant (String participantId, Boolean canReceive, Boolean isSender){}

  private Participant participants;

  public Participant getParticipants() {
    return participants;
  }

  public void setParticipants(Participant participants) {
    this.participants = participants;
  }
}
