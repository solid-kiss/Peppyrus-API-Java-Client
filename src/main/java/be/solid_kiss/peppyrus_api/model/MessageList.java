package be.solid_kiss.peppyrus_api.model;

import java.util.Set;

public class MessageList {

  /**
   * @param pages
   * @param currentPage
   * @param itemCount
   */
  public static record Meta(int pages, int currentPage, int itemCount){}

  private Set<Message> items;

  private Meta meta;

  public Set<Message> getItems() {
    return items;
  }

  public void setItems(Set<Message> items) {
    this.items = items;
  }

  public Meta getMeta() {
    return meta;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }
}
