package kz.iitu.diploma.impl;

import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.inservice.search_engine.google.impl.GoogleSearchServiceReal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DiplomaApplicationTests extends AbstractTestParent{

    @Autowired
    private GoogleApiConfig googleApiConfig;

    @Test
    public void google_searchService() {
        GoogleSearchServiceReal googleSearch = new GoogleSearchServiceReal(googleApiConfig);
        //    Mockito.when(googleSearch.search("Test")).then()
        var res = googleSearch.search("наркотики,сигареты");
    }

}
