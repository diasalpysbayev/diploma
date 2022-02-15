package kz.iitu.diploma.inservice.instagram.impl;

import kz.iitu.diploma.inservice.instagram.InstagramService;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchTagsResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

public class InstagramServiceFake implements InstagramService {

  @Override
  public InstagramSearchUsernameResult searchByUsername() {
    return null;
  }

  @Override
  public InstagramSearchTagsResult searchByTags() {
    return null;
  }
}
