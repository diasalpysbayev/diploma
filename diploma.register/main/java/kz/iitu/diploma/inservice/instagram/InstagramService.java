package kz.iitu.diploma.inservice.instagram;

import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchTagsResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

public interface InstagramService {

  InstagramSearchUsernameResult searchByUsername();

  InstagramSearchTagsResult searchByTags();

}
