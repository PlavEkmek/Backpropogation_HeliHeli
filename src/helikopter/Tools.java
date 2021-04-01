package helikopter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
 * Normalizasyon
 * Rastgele Test ve Kontrol Veri Seti Olustuma
 * islemleri yapan sinif
 */
public class Tools {
	
	private static final File data = new File(Tools.class.getResource("Data.txt").getPath());
	private File ndata = null; 
	//Daha onceden normalize olmus dosyada islem yapilmamasi icin
	public boolean ISNormalized = false;
	
	public double MaxYer = 9.8;
	public double MinYer = 9.76;
	
	public double MaxAgir = 56;
	public double MinAgir = 2.2;
	
	public double MaxRpm = 132;
	public double MinRpm = 0;
	
	public Tools() throws IOException {
		if(!ISNormalized) Normalize();
	}
	public void Normalize() throws IOException {
		Scanner oku = new Scanner(data);
		ndata= new File("NData.txt");
		if (!ndata.exists()) {
			ndata.createNewFile();             
		}
		
		FileWriter yazici = new FileWriter(ndata);
		
		double norm;
		
		while(oku.hasNext())
		{
			norm = (oku.nextDouble() - MinYer)/(MaxYer - MinYer);
			yazici.write(norm +" ");
			
			norm = (oku.nextDouble() - MinAgir)/(MaxAgir - MinAgir);
			yazici.write(norm +" ");
			
			norm = (oku.nextDouble() - MinRpm)/(MaxRpm - MinRpm);
			yazici.write(norm +" ");
			
			yazici.write(oku.nextInt()+"\n");
		}
		ISNormalized = true;
		oku.close();
		yazici.close();
	}
	public void TestHazirla() throws IOException {
		if(!ISNormalized) 
		{
			System.out.print("Dosya normalize degil");
			return;
		}
		ArrayList<String> nlist = new ArrayList<String>();
		
		Scanner oku = new Scanner(ndata);
		FileWriter egit = new FileWriter("EgitData.txt");
		FileWriter test = new FileWriter("TestData.txt");
		
		//Tüm verilerin listeye satır satır aktarılması
		while(oku.hasNext()) {
			nlist.add(oku.nextLine());
		}
		oku.close();
		
		Random rnd=new Random();
		int indis=0;
		//Test verilerinin rastgele seçilimi ve dosyaya yazılması
		for(int k = 0; k < 300;k++) 
		{
			indis=rnd.nextInt(50000);
			if(nlist.get(indis)=="A") {
				k--;
				continue;
			}
			test.write(nlist.get(indis) + "\n");
			nlist.remove(indis);
			nlist.add(indis, "A");
		}	
		test.close();
		
		//Eğitim verilerinin rastgele seçilip yazılması
		for(int i = 0; i < 700;i++)
		{
			indis = rnd.nextInt(50000);
			if(nlist.get(indis)=="A") {
				i--;
				
				continue;
			}
			egit.write(nlist.get(indis)+"\n");
		}
		egit.close();
	}

}
