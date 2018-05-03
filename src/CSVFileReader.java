import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Add code here to manipulate data set. If an object of this class is created it calls load() to read the complete.csv file.
 * The rows are saved in the ArrayList 'rows'.
 */

public class CSVFileReader {

    private final static String fileName = "complete.csv";
    private final static String CSVSeparator = ",";
    private final static String processedFile = "processedFIFA18.csv";
    private final static String processedFileSuperClass = "processedFileSuperClassFIFA18.csv";
    private final static String processedFileSubset = "processedSubsetFIFA18.csv";
    private final static String PREFVAL = "True";

    private final static int numSubset = 100; //number of samples for the test subset

    private final static String[] VALUES = {"prefers_cb", "prefers_rb", "prefers_lb", "prefers_rwb", "prefers_lwb", "prefers_cdm",
            "prefers_cm", "prefers_cam", "prefers_lm", "prefers_rm", "prefers_lw", "prefers_rw", "prefers_st", "prefers_cf"};
    private ArrayList<CSVRow> rows;
    private ArrayList<CSVRow> rowsSuperClass;

    private HashMap<String, Integer> map;


    public CSVFileReader(){
        try {
            rows = new ArrayList<>();
            rowsSuperClass = new ArrayList<>();
            map = CSVRow.getAttributeMap();
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() throws IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(fileName));
        try {
            //StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while ((line = br.readLine())!= null) {
                //sb.append(line);
                //sb.append(System.lineSeparator());
                String[] row = line.split(CSVSeparator);
                rows.add(new CSVRow(row));
            }
            //String everything = sb.toString();
            //System.out.println(everything);

        } finally {
            br.close();
        }
    }

    public void createSubset(){
        try  {
            PrintWriter writer = new PrintWriter(new File(processedFileSubset));
            StringBuilder sb = new StringBuilder();
            sb.append(CSVRow.attributes+"\n");
            for(int i = 0; i < numSubset; i++){
                sb.append(rows.get(i).toString()+"\n");
            }
            writer.write(sb.toString());
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void removeGKsAddPos(){
        int GKCol = map.get("gk");
        ArrayList<CSVRow> remove = new ArrayList<>();
        for(CSVRow row : rows){
            if(!row.getRow()[GKCol].matches("")){
                System.out.println(row.toString());
                remove.add(row);
            }else{
                String[] prefs = getPrefs(row);
                addPos(row, prefs);
            }
        }
        rows.removeAll(remove);
    }

    /**
     *Add position according to Kai's RapidMiner process
     * @param row the row to change.
     * @param prefs the position preferences from the data set.
     */
    private void addPos(CSVRow row, String[] prefs){
          if (prefs[0].matches(PREFVAL)){
            rowsSuperClass.add(new CSVRow(row.addColumnReturn("cb")));
          }
          if(prefs[1].matches(PREFVAL) || prefs[2].matches(PREFVAL) ||prefs[3].matches(PREFVAL) || prefs[4].matches(PREFVAL)){
              rowsSuperClass.add(new CSVRow(row.addColumnReturn("wb")));
          }
          if(prefs[5].matches(PREFVAL) || prefs[6].matches(PREFVAL) || prefs[7].matches(PREFVAL)){
              rowsSuperClass.add(new CSVRow(row.addColumnReturn("mid")));
          }
          if(prefs[8].matches(PREFVAL) || prefs[9].matches(PREFVAL) ||prefs[10].matches(PREFVAL) || prefs[11].matches(PREFVAL)){
              rowsSuperClass.add(new CSVRow(row.addColumnReturn("win")));
          }
          if(prefs[12].matches(PREFVAL) || prefs[13].matches(PREFVAL)){
              rowsSuperClass.add(new CSVRow(row.addColumnReturn("cf")));
          }
    }


    /**
     *
     * @return Returns array with preferred position values. Value can be True or False
     */
    private String[] getPrefs(CSVRow row){
        String[] prefs = new String[VALUES.length];
        for(int i = 0; i<VALUES.length; i++){
            prefs[i] = row.getRow()[map.get(VALUES[i])];
        }
        return prefs;
    }

    /**
     * Call removeGKsAddPos() first.
     * Write File where set does not contain gks.
     */
    public void writeFile(){
        try  {
            PrintWriter writer = new PrintWriter(new File(processedFile));
            StringBuilder sb = new StringBuilder();
            sb.append(CSVRow.attributes+"\n");
            for(CSVRow row : rows){
                sb.append(row.toString()+"\n");
            }
            writer.write(sb.toString());
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * Creates set that contains the attribute position. Call removeGKsAddPos() first.
     */
    public void writeFileSuperClass(){
        try  {
            PrintWriter writer = new PrintWriter(new File(processedFileSuperClass));
            StringBuilder sb = new StringBuilder();
            sb.append(CSVRow.newAttributes+"\n");
            for(CSVRow row : rowsSuperClass){
                sb.append(row.toString()+"\n");
            }
            writer.write(sb.toString());
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Call removeGKsAddPos() first.
     * takes the first #rows = numSubSet for the subset and writes it into a .csv file.
     * the set contains the attribute position.
     */
    public void writeFileSuperClassSubSet(){
        try  {
            PrintWriter writer = new PrintWriter(new File(processedFileSubset));
            StringBuilder sb = new StringBuilder();
            sb.append(CSVRow.newAttributes+"\n");
            for(int i = 0; i < numSubset; i++){
                sb.append(rowsSuperClass.get(i).toString()+"\n");
            }
            writer.write(sb.toString());
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public ArrayList<CSVRow> getRows() {
        return rows;
    }
}
