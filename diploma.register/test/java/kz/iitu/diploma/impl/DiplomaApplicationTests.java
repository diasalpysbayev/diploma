package kz.iitu.diploma.impl;

import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.inservice.search_engine.google.impl.GoogleSearchServiceReal;
import kz.iitu.diploma.model.query.QueryRecord;
import kz.iitu.diploma.model.search_engine.GoogleResult;
import kz.iitu.diploma.register.QueryRegister;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class DiplomaApplicationTests extends AbstractTestParent{

    @Autowired
    private GoogleApiConfig googleApiConfig;

    @Autowired
    private QueryRegister queryRegister;

    @Test
    public void google_searchService() {
        GoogleSearchServiceReal googleSearch = new GoogleSearchServiceReal(googleApiConfig);
        //    Mockito.when(googleSearch.search("Test")).then()
        var res = googleSearch.search("наркотики,сигареты");
    }

    @Test
    public void test() {
        QueryRecord queryRecord = new QueryRecord();
        queryRecord.queryList = new ArrayList<>();
        queryRecord.queryList.add("Наркотики");
        queryRecord.queryList.add("Сон");
        queryRecord.queryList.add("Сигареты");

        //
        //
        queryRegister.executeQuery(queryRecord);
        //
        //
    }

}
