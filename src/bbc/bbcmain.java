package bbc;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class bbcmain {
	
	public static void main(String[] args) {
		String folderPath;
		String destinPath="";
		String extension;
		BufferedImage src, dest;
			String name, xmins, ymins,xmaxs,ymaxs;
			int xmin, ymin,xmax,ymax;
			int width, height;
    	BufferedReader reader;
    	String line;
		int numofimages = 0 ;
		int numofcreatedimages = 0 ;
		int flag = 0;
		int cnt =0;
		
		System.out.println(  "*********************************************************\n"
							+"<<<<<<<<<<<<<<<<< Bounding Box Cropping >>>>>>>>>>>>>>>>>\n"
							+"*this program is for converting images to cropped images*\n"
							+"*********************************************************\n"
							+"\n"
							+"*********************************************************\n"
							+"*****************  Author: Heemoon Yoon  ****************\n"
							+"**************** heemoon.yoon@utas.edu.au ***************\n"
							+"***************** UNIVERSITI OF TASMANIA ****************\n"
							+"******************** DATE 14/NOV/2019 *******************\n"
							+"*********************************************************\n"
							+"\n"
							+"                         [Warning]\n"
							+ "    image file has to be the same name with xml file\n"
							+ "           image file format has to be unified\n"
							+ "\n"
							+ "*********************************************************\n"
							+ "Enter folder path which includes images & xml\n"
							+ "(ex : C:/Users/user/Documents/)\n"
							+ " : ");
		
		//get target folder
		Scanner sc = new Scanner(System.in);
		folderPath = sc.nextLine();
		if(!folderPath.endsWith("/"))
			folderPath = folderPath + "/";
		File folder = new File(folderPath);
		
		//get list of files in the folder
		File[] listOfFiles = folder.listFiles();
		numofimages = listOfFiles.length / 2;
		
		//test folder validity
		if(listOfFiles.length<1){
			System.out.println("The folder is empty! (or it's not valid folder path)");
		}else if(listOfFiles.length%2 != 0){
			System.out.println("[warning] Some files are not pair");
		}else {
			
		//if folder is valid -> get extension
		String filename = listOfFiles[0].getName();
		extension = filename.substring(filename.lastIndexOf('.'), filename.length());
		System.out.println("\nextension type : "+ extension);
		
		//check extension type
		while(true){
			System.out.println(extension + "<- Is this your images' extension type? [Y|N]: ");
			String answer = sc.nextLine();
			if(answer.equals("N") || answer.equals("n") || answer.equals("no") || answer.equals("NO") || answer.equals("No")){
				System.out.println("The 'FIRST' file in your folder must be image file\n"
						+ "please adjust your files(name) and try again.");
				break;
			}else if(answer.equals("Y") || answer.equals("y") || answer.equals("yes") || answer.equals("YES") || answer.equals("Yes")){
				//get destin Path
				System.out.println("\nEnter Saving folder path\n"
							  	 + "(ex : C:/Users/user/Documents/)\n"
								 + "Path : ");
				destinPath = sc.nextLine();
				if(!destinPath.endsWith("/"))
					destinPath = destinPath + "/";
				//make directory if not exists
				File destinfile = new File(destinPath);
				if(!destinfile.exists()) {
					if (!destinfile.mkdir()) {
						System.out.println("Invalid saving folder Path!");
						sc.nextLine();
					}
				}
				flag = 1;
				break;
			}else{
				System.out.println("Invalid input: " + answer);
			}
		}
		//iterate files in folder
		if(flag ==1){
		for (File file : listOfFiles) {
			if (file.getName().endsWith(".xml")) {
				try{
					cnt++;
					//read xml file
					reader = new BufferedReader(new FileReader(file));
					line = reader.readLine();
					int numb = 0;
					while(line != null){
						//for each object
					    if(line.endsWith("</name>")){
					    	name = line.substring(0, line.lastIndexOf('<'));
					    	name = name.substring(name.lastIndexOf('>')+1);
					    	name = name.trim();
						    reader.readLine(); //<pose>Unspecified</pose>
						    reader.readLine(); //<truncated>0</truncated>
						    reader.readLine(); //<difficult>0</difficult>
						    reader.readLine(); //<bndbox>
						    line = reader.readLine(); //<xmin>488</xmin>
							    xmins = line.substring(0, line.lastIndexOf('<'));
							    xmins = xmins.substring(xmins.lastIndexOf('>')+1);
							    xmin = Integer.parseInt(xmins);
						    line = reader.readLine(); //<ymin>263</ymin>
								ymins = line.substring(0, line.lastIndexOf('<'));
							    ymins = ymins.substring(ymins.lastIndexOf('>')+1);
							    ymin = Integer.parseInt(ymins);
							line = reader.readLine(); //<xmax>668</xmax>
							    xmaxs = line.substring(0, line.lastIndexOf('<'));
							    xmaxs = xmaxs.substring(xmaxs.lastIndexOf('>')+1);
							    xmax = Integer.parseInt(xmaxs);
							line = reader.readLine(); //<ymax>510</ymax>
							    ymaxs = line.substring(0, line.lastIndexOf('<'));
							    ymaxs = ymaxs.substring(ymaxs.lastIndexOf('>')+1);
							    ymax = Integer.parseInt(ymaxs);
							width = xmax - xmin;
							height = ymax - ymin;
							
							//*write image file*//
							//set src image path
							String filepathwithoutextension = FilePathToWithoutExtension(file);
							String srcfilepath = filepathwithoutextension+extension;
							src = ImageIO.read(new File(srcfilepath));
							
							//cropping
							dest = src.getSubimage(xmin, ymin, width, height);
							
							//save image with modified name
							numb++;
							filename = filepathwithoutextension.substring(filepathwithoutextension.lastIndexOf('\\')+1, filepathwithoutextension.length());
							
							//folder create if not exists
								File directory = new File(destinPath + name);
								if (! directory.exists())
									directory.mkdir();
							
							//write file
							File outputfile = new File(destinPath + name +"/" +filename+"_"+name+"_"+numb+extension);
							ImageIO.write(dest, extension.substring(1), outputfile);
							
							System.out.println("["+cnt+"/"+numofimages+"][created]"+destinPath + name +"/" +filename+"_"+name+"_"+numb+extension);
							numofcreatedimages++;
					    }
					    line = reader.readLine();
					}
				} catch (IOException e){
					System.out.println(e.getMessage());
				} 
		    }
		}
				
		//summary
		System.out.println("\n<<<<<<<<<<<<<<<<< SUMMARY >>>>>>>>>>>>>>>>>\n"
						 + "Total Read images: "+ numofimages +"\n"
						 + "Total created images: "+numofcreatedimages+"\n"
						 + "*******************************************\n");
		}}
		
		//exit program
		System.out.println("Press Any key to close");
		sc.nextLine();
	}
	
	private static String FilePathToWithoutExtension(File file){
		String result;
		String absolutePath = file.getAbsolutePath();
		result = absolutePath.substring(0, absolutePath.lastIndexOf('.')); 
		return result;
	}
}