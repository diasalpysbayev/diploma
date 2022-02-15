package kz.iitu.diploma.impl;

import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.inservice.google.GoogleSearchService;
import kz.iitu.diploma.inservice.google.impl.GoogleSearchServiceReal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

public class DiplomaApplicationTests extends AbstractTestParent{

    @Autowired
    private GoogleApiConfig googleApiConfig;

    @Test
    public void google_searchService() {
        GoogleSearchServiceReal googleSearch = new GoogleSearchServiceReal(googleApiConfig);
        //    Mockito.when(googleSearch.search("Test")).then()
        var res = googleSearch.search("Dias Alpysbayev");
    }

}
