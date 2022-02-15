package kz.iitu.diploma.inservice.instagram.impl;

import kz.iitu.diploma.config.InstagramConfig;
import kz.iitu.diploma.inservice.instagram.InstagramService;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchTagsResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

public class InstagramServiceReal implements InstagramService {

  private final InstagramConfig instagramConfig;

  public InstagramServiceReal(InstagramConfig instagramConfig) {
    this.instagramConfig = instagramConfig;
  }

  @Override
  public InstagramSearchUsernameResult searchByUsername() {
    return null;
  }

  @Override
  public InstagramSearchTagsResult searchByTags() {
    return null;
  }
}
