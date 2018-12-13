package com.bookexchange.app.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SearchHelper {

    private @Getter @Setter
    Directory directory; // later to be changed to FS directory
    private @Getter @Setter IndexWriter indexWriter;
    private static SearcherManager searcherManager;
    private ScheduledExecutorService executorService;
    private QueryParser queryParser;
    private static final String searchField = "title";
    private static final String retrieveField = "bid";

    private static final String INDEX_DIRECTORY = "./testIndex";

    public SearchHelper() throws IOException {
        this.directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        this.indexWriter = new IndexWriter(this.directory, new IndexWriterConfig(new SmartChineseAnalyzer()));
        this.indexWriter.commit();
        this.queryParser = new QueryParser("title", new SmartChineseAnalyzer());
        SearchHelper.searcherManager = new SearcherManager(this.directory, null);
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.startFutureThread();
    }

    private void startFutureThread() {
        this.executorService.scheduleWithFixedDelay(() -> {
            try {
                this.indexWriter.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.SECONDS);

        this.executorService.scheduleWithFixedDelay(() -> {
            try {
                SearchHelper.searcherManager.maybeRefresh();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private Document newDoc(int bid, String title) {
        Document document = new Document();
        document.add(new TextField(SearchHelper.searchField, title, Field.Store.YES));
        document.add(new LongPoint(SearchHelper.retrieveField, bid));
        document.add(new StoredField(SearchHelper.retrieveField, bid)); // duplication is necessary
        return document;
    }

    public void show() throws IOException {
        IndexSearcher indexSearcher = SearchHelper.searcherManager.acquire();
        int count = indexSearcher.count(new MatchAllDocsQuery());
        if (count > 0) {
            TopDocs topDocs = indexSearcher.search(new MatchAllDocsQuery(), count);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                System.out.println(indexSearcher.doc(scoreDoc.doc));
            }
        }
        System.out.println("=================================");
        SearchHelper.searcherManager.release(indexSearcher);
    }

    public List<Integer> query(String queryStr, int page, int pageSize) throws IOException, ParseException {
        if (page <= 0 || pageSize <= 0) return null;
        List<Integer> bookIdList = new ArrayList<>(pageSize);
        IndexSearcher indexSearcher = SearchHelper.searcherManager.acquire();
        Query query = this.queryParser.parse(queryStr);
        TopDocs topDocs = indexSearcher.search(query, 10);
//        System.out.println("found" + topDocs.totalHits);
//        System.out.println("all we found " + Arrays.toString(topDocs.scoreDocs));

//        System.out.println("~~~~~~~~~~~pagination~~~~~~~~~~~~");
        int curBid;
        int startOffset = (page-1)*pageSize;
        int endOffset = page*pageSize;
        if (topDocs.scoreDocs.length-1 >= startOffset) {
            endOffset = Math.min(topDocs.scoreDocs.length, endOffset);
            for(int i = startOffset; i < endOffset; i++) {
                Document doc = indexSearcher.doc(topDocs.scoreDocs[i].doc);
                curBid = Integer.parseInt(doc.get("bid"));
                bookIdList.add(curBid);
//                System.out.println("get " + doc.get("title") + " " + curBid + " " + topDocs.scoreDocs[i].score);
            }
        }
        SearchHelper.searcherManager.release(indexSearcher);

        return bookIdList;
    }

    public void addNewDocument(int bid, String title) throws IOException {
        this.indexWriter.addDocument(this.newDoc(bid, title));
    }

    public void deleteDocument(int bid) throws IOException {
        this.indexWriter.deleteDocuments(LongPoint.newExactQuery(SearchHelper.retrieveField, bid));
    }

    public void updateDocument(int bid, String title) throws IOException {
        this.indexWriter.deleteDocuments(LongPoint.newExactQuery(SearchHelper.retrieveField, bid));
        this.indexWriter.addDocument(this.newDoc(bid, title));
    }

//    static class RefreshThread implements Runnable {
//        @Override
//        public void run() {
//            try {
//                searcherManager.maybeRefresh();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
