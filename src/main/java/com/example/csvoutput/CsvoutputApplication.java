package com.example.csvoutput;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class CsvoutputApplication {

	public static void main(String[] args)throws Exception {
		SpringApplication.run(CsvoutputApplication.class, args);
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		CSVWriter writer = new CSVWriter(new FileWriter("PATH_TO_YOUR_OUTPUT_DIRECTORY//csvoutput//storage//output.csv"));
		File[] files = new File("PATH_TO_YOUR_INPUT_DIRECTORY").listFiles();
		try
		{
			List list = new ArrayList();
			String line1[] = {"id","File Name","Method Name & Arguments"};
			list.add(line1);
			String line= " ";
			int count = 0,i;
			
			for(File file : files)
			{
				if(file.isFile())
				{
					count++;
					FileReader fr = new FileReader(file);
					br = new BufferedReader(fr);
					while((line = br.readLine()) != null)
					{
						if(filterLines(line) != "") 
						{
							String line2[] = {Integer.toString(count),file.getName(),filterLines(line).trim()};
							list.add(line2);
						}
					}
				}
			}
			writer.writeAll(list);
			writer.flush();
			System.out.println("Data entered");
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	static String filterLines(String line) {
		String out = "";
		List<String> method = new ArrayList<>();
		Pattern reMethod= Pattern.compile("(public|private|protected)(.*\\)).*\\{");
		Matcher matchMethod = reMethod.matcher(line);
		while (matchMethod.find()) {
			method.add(matchMethod.group());
		}
		if(method.size() > 0){
			out = line.replaceAll("(\\w+)\\((.*)\\)", "method name: $1, parameters: $2");
			
		}
		return out;
	}
}


