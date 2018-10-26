import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class Test {

  @org.junit.Test
  public void queryByAll() throws Exception {
    Query query = new MatchAllDocsQuery();
    doQuery(query);
  }

  private void doQuery(Query query) throws Exception {
    DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("H:\\aaa")));
    IndexSearcher searcher = new IndexSearcher(reader);

    TopDocs topDocs = searcher.search(query, 20);
    System.out.println("命中文件数量" + topDocs.totalHits);
      ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for (ScoreDoc scoreDoc : scoreDocs) {
      System.out.println("当前的文档得分为" + scoreDoc.score);
      System.out.println("当前文档的id" + scoreDoc.doc);
        Document doc = searcher.doc(scoreDoc.doc);
      System.out.println("文件名称" + doc.get("fileName"));
      System.out.println("文件内容" + doc.get("fileContent"));
      System.out.println("文件路径" + doc.get("filePaht"));
      System.out.println("文件大小" + doc.get("fileSize"));
    }
  }


  @org.junit.Test
  //根据词条查询对象
  public void queryByTerm() throws Exception {
      TermQuery query = new TermQuery(new Term("fileName", "fff"));
      doQuery(query);
  }

  @org.junit.Test
  public void queryByRange() throws Exception {
      Query query = NumericRangeQuery.newLongRange("fileSize", 1L, 10L, true, true);
      doQuery(query);
  }

  @org.junit.Test
  public void queryByBoolean() throws Exception {
      TermQuery query = new TermQuery(new Term("fileName", "fff"));
      BooleanQuery booleanQuery = new BooleanQuery();
      booleanQuery.add(query, BooleanClause.Occur.SHOULD);
      doQuery(booleanQuery);
  }

  @org.junit.Test
  public void queryByParse() throws Exception {
      String searchStr = "fff立法txt";
      QueryParser parser = new QueryParser("fileName", new IKAnalyzer());
      Query parse = parser.parse(searchStr);
      doQuery(parse);
  }

  @org.junit.Test
  public void queryByMultiParse() throws Exception {
      String searchStr = "fff米sdaf";
      String[] strings = {"fileName", "fileContent"};
      MultiFieldQueryParser parser = new MultiFieldQueryParser(strings, new IKAnalyzer());
      Query query = parser.parse(searchStr);
      doQuery(query);
  }


}
