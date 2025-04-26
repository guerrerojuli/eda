package core;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

import eda.IndexParametricService;
import eda.IndexWithDuplicates;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;


public class DataAnalysis {
    public static void main(String[] args) throws IOException {
    	
	    // leemos el archivo
    	
    	/*
       	// opcion 1:  probar con  / o sin barra
	    URL resource = DataAnalysis.class.getClassLoader().getResource("co_1980_alabama.csv");
	    Reader in = new FileReader(resource.getFile());
    	*/
    	
    	
    	/*
    	// opcion 2:  probar con  / o sin barra
        URL resource= DataAnalysis.class.getResource("/co_1980_alabama.csv");
   	    Reader in = new FileReader(resource.getFile());
    	*/
    	
    	/*
    	// opcion 3: probar con / o sin barra
    	String fileName= "/co_1980_alabama.csv";
    	InputStream is = DataAnalysis.class.getClass().getResourceAsStream(fileName);
    	Reader in = new InputStreamReader(is);
    	*/
    	
    	/*
  		// opcion 4 
 		String fileName= "/co_1980_alabama.csv"; 
 		InputStream is = DataAnalysis.class.getResourceAsStream(fileName );
 		Reader in = new InputStreamReader(is); 	
    	 */
    	
    	
    	// opcion 5 
   		String fileName= "co_1980_alabama.csv"; 
   		InputStream is = DataAnalysis.class.getClassLoader().getResourceAsStream(fileName);
   		Reader in = new InputStreamReader(is); 			
    	
    	
 		CSVFormat theCSV = CSVFormat.DEFAULT.builder()
   				.setHeader()
   				.setSkipHeaderRecord(true)
   			    .get();

   		Iterable<CSVRecord> records = theCSV.parse(in);
   		
   		// Genero las estructuras
   		
	    // Coleccion de valores
	    HashMap<Long, CSVRecord> datos= new HashMap<>();
	    
	    IndexParametricService<IdxRecord<Double, Long>> indexPolucion;
	    
	    // Indice sobre polucion sin reflection
	    indexPolucion= new IndexWithDuplicates<>();
	    
	    // Indice sobre polucion con reflection
	    // indexPolucion = new IndexWithDuplicates<>(IdxRecord.class);

	    
	    
	    // Pueblo con los valores ambas estructuras
	    
	    // coleccion de datos
	    for (CSVRecord record : records) {
	    	// insertamos en la coleccionIdxRecord<Double, Long>IdxRecord<Double, Long>
	    	datos.put(record.getRecordNumber(), record);
	    	
	    	// insertamos lo minimo en el indice
	        String value = record.get("daily_max_8_hour_co_concentration");
	        long id = record.getRecordNumber();
	        indexPolucion.insert(new IdxRecord<>( Double.valueOf(value), id ));
	    }
	    in.close();
	    
	    // ACA EMPIEZA las QUERIES

		// Promedio de polucion
		double promedio	= 0;
		int count=0;
		for (CSVRecord record : records) {
			String value = record.get("daily_max_8_hour_co_concentration");
			promedio += Double.valueOf(value);
			count++;
		}
		promedio /= count;
		System.out.println("Promedio de polucion: " + promedio);

		// Imprimir ascendentemente la info por polucion
		indexPolucion.sortedPrint();

		indexPolucion.search(new IdxRecord<>(2.8, 0L));
    }
}
