package xqt.adapters.builtin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.vaiona.commons.logging.LoggerHelper;

public class TestingReader {
	static final int SIZE = 8 * 1024;
	private byte[] buffer = new byte[SIZE];
    Boolean found = false;
	long max = Long.MIN_VALUE;
	long lineNo = 0;
	
	// reads the input file, computes the max of values of the column 0 that are integer numbers.
	public void readSenario1(String filePath) throws IOException{
		FileInputStream fileInputStream = new FileInputStream(filePath);
		//...
		 try {
			 ScanDataFile(fileInputStream);
	    } catch(Exception ex) {
	        ex.printStackTrace();
	        LoggerHelper.logInfo(ex.getMessage());
	    }
		String s = "The max is " + max + ". " + lineNo + " lines were read.";
		System.out.println(s);
		LoggerHelper.logInfo(s);
	}

	private void ScanDataFile(FileInputStream f) throws FileNotFoundException, IOException {
	  // Use a mapped and buffered stream for best speed.
	  FileChannel channel = f.getChannel();
	  long red = 0L;
	  do {
	    long read = Math.min(Integer.MAX_VALUE, channel.size() - red);
	    MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY, red, read);
	    int nGet;
	    while (mb.hasRemaining()) {
	      nGet = Math.min(mb.remaining(), SIZE);
	      mb.get(buffer, 0, nGet);
	      consumeBuffer1(nGet);
	    }
	    red += read;
	  } while (red < channel.size());
	  // Finish off.
	  channel.close();
	  f.close();
	}	
	
	private void consumeBuffer1(int len){
		byte[] localBuffer = new byte[SIZE];
		long temp = Long.MIN_VALUE;
        int bufferIndex = 0;
        
	      for (int i = 0; i < len; i++) {
	        byte get = buffer[i];
	        if(get == ',' && !found) { // look for the first column value only
	        	String columnValue = new String(localBuffer, 0, bufferIndex);
	        	found = true;
	        	try{
	        		temp = Long.parseLong(columnValue);
	        		if(temp < 0){ //selectivity 0%, < 1000000 for 1%, <10000000 for 10% ...
	        			if(temp > max){
	        				max = temp;
	        			}
	        		}
	        		temp = Long.MIN_VALUE;
	        	} catch(Exception ex){
	        		// do nothing
	        	}
	        } else if(get == '\n') {
	            for(int j = 0; j < bufferIndex; j++)
	            	localBuffer[j] = 0;
	            lineNo++;
	            bufferIndex = 0;
	            found = false;
	        } else
	        	localBuffer[bufferIndex++] = get;
		}
      }
}
