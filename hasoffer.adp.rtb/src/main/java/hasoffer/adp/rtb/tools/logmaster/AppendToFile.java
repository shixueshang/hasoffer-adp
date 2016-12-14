package hasoffer.adp.rtb.tools.logmaster;


import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Appends stuff to a file
 * @author Ben M. Faul
 */
public class AppendToFile {

	public static void item(String fileName, StringBuilder sb)
			throws Exception {

		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(fileName, true));
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

} 
