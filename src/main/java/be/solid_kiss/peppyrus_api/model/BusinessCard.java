package be.solid_kiss.peppyrus_api.model;

import java.util.Set;

public class BusinessCard {

  public static record BusinessCardParticipant (String scheme, String identifier) {}

  private BusinessCardParticipant participant;

  private Set<BusinessCardEntity> entities;
}
