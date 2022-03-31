package kz.iitu.diploma.impl;

import kz.iitu.diploma.config.GoogleApiConfig;
import kz.iitu.diploma.inservice.search_engine.google.impl.GoogleSearchServiceReal;
import kz.iitu.diploma.model.query.QueryDetail;
import kz.iitu.diploma.model.query.QueryRecord;
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
        QueryDetail queryDetail = QueryDetail.builder().name("Здоровый сон").isVideo(true).build();
//        queryRecord.queryList.add("Алкоголь вредный ли");
//        queryRecord.queryList.add("Здоровый сон");
//        queryRecord.queryList.add("Сигареты вред");
        queryRecord.queryList.add(queryDetail);

        //
        //
        queryRegister.executeQuery(queryRecord);
        //
        //
    }

}
