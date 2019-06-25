import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.file.Paths;



public class Indexer {
    public static void main(String[] args) {
        Indexer indexer = new Indexer();
        try {
            indexer.index();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void index() throws Exception {
        Directory directory = FSDirectory.open(Paths.get("E:\\github\\temp\\index"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        File f = new File("E:\\github\\php7cc");
        File[] fileList = f.listFiles();
        for (File file : fileList) {
            if (file.isFile()) {
                Document document = new Document();

                String file_name = file.getName();
                Field fileNameFiled = new TextField("fileName", file_name, Field.Store.YES);

                long file_size = FileUtils.sizeOf(file);
                Field fileSizeFiled = new LongField("fileSize", file_size, Field.Store.YES);

                String file_path = file.getPath();
                Field filePathFiled = new StoredField("filePath", file_path);

                String file_content = FileUtils.readFileToString(file);
                Field fileContentField = new TextField("fileContent", file_content, Field.Store.NO);

                document.add(fileNameFiled);
                document.add(fileSizeFiled);
                document.add(filePathFiled);
                document.add(fileContentField);

                indexWriter.addDocument(document);
            }
        }

        indexWriter.close();
    }
}
