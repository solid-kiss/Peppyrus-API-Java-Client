package be.solid_kiss.peppyrus_api.model;

import java.util.Set;

public class MessageReport {

  private Set<ValidationRule> validationRules;

  private String transmissionRules;

  public Set<ValidationRule> getValidationRules() {
    return validationRules;
  }

  public void setValidationRules(Set<ValidationRule> validationRules) {
    this.validationRules = validationRules;
  }

  public String getTransmissionRules() {
    return transmissionRules;
  }

  public void setTransmissionRules(String transmissionRules) {
    this.transmissionRules = transmissionRules;
  }
}
