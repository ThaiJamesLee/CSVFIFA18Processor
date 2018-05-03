import java.util.ArrayList;

/**
 * This was written to process the complete.csv file and is not generic.
 * I am fully aware that this code is poorly written.
 *
 * Comment and uncomment to create the preferred .csv file.
 * It is not recommended to run all at the same time, since an Exception can be thrown due to insufficient heap size.
 */
public class Main {

    public static void main(String[] args){
        CSVFileReader reader = new CSVFileReader();
        ArrayList<CSVRow> rows = reader.getRows();
        reader.removeGKsAddPos();
        //reader.writeFile();
        //reader.writeFileSuperClass();
        reader.writeFileSuperClassSubSet();
        //reader.createSubset();
    }
}
