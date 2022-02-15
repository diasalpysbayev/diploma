package kz.iitu.diploma.inservice.instagram;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchTagsRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchLocationsResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchTagsResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchTagsResultTag;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;

import java.io.IOException;

public class Instagram {

  public static void main(String[] args) throws IOException {
    Instagram4j insta = Instagram4j.builder().username("diasalpysbayev").password("Aurum753335").build();

    insta.setup();
    insta.login();

    InstagramSearchLocationsResult locationsResult = new InstagramSearchLocationsResult();
    InstagramSearchUsernameResult  usernameResult  = insta.sendRequest(new InstagramSearchUsernameRequest("nnuri_02"));

    InstagramSearchTagsResult      tagsResult      = insta.sendRequest(new InstagramSearchTagsRequest("almaty"));
    tagsResult.getResults();
    usernameResult.getUser();

  }

}
