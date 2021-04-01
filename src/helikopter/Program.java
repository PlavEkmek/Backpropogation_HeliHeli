/*
 * Uğur Çelik 27.12.2020
 * 
 */
package helikopter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) throws IOException {
		int secim= 0;
		Tools tl = new Tools();
		Scanner ara  = new Scanner(System.in);
		YSA ysa = new YSA(tl,8000);
		do {
			System.out.println("1-Test Hazirla");
			System.out.println("2-Agi Egit");
			System.out.println("3-Agi Test Et");
			System.out.println("4-Min Hata Oranı");
			System.out.println("5-Epochtaki Hatalar");
			System.out.println("6-Test Verisi ile Test Et");
			System.out.println("7-Momentum Egit");
			System.out.println("8-Momentum Agi Test Et");
			System.out.println("9-Momentum Min Hata Orani");
			System.out.println("10-Momentumu Test Verisi ile Test Et");
			System.out.println("11-Cikis");
			
			System.out.print("=>");
			secim=ara.nextInt();
			switch(secim) {
			case 1:
				tl.TestHazirla();
				System.out.println("Test ve Egitim Verileri Hazir!");
				break;
			case 2:
				ysa.Egit();
				break;
			case 3:
				System.out.print("yer cekimi[9.76-9.8]=>");
				double x = ara.nextDouble();
				System.out.print("agirlik[2.2-56]=>");
				double y = ara.nextDouble();
				System.out.print("rpm=>[0-132]"); 
				double z = ara.nextDouble();
				double[] cikti = ysa.test(x, y, z);
				System.out.println("Output:" + cikti[0]);
				break;
			case 4:
				System.out.println("Hata Oranı: "+ysa.EgitimSonHata());
				break;
			case 5:
				double[] hatalar = ysa.Hatalar();
				int epoch = 1;
				FileWriter fl = new FileWriter("EpochHatalari.txt");
				for(double h: hatalar) {
					System.out.println(epoch+":"+h);
					fl.write(epoch+":"+h+"\n");
					epoch++;
				}
				fl.close();
				break;

			case 6:
				YSA.TestOut out = ysa.new TestOut();
				out=ysa.TestVerisiIleSonuc();
				System.out.println("Rastgele test verisi ile test sonucu");
				System.out.println("yer cekimi:"+out.yer);
				System.out.println("agirlik:"+out.agirlik);
				System.out.println("rpm:"+out.rpm);
				System.out.print("Ag sonucu: "+out.sinirselCikti[0]+"\n");
				break;
			case 7:
				ysa.MomentumEgit();
				break;
			case 8:
				System.out.print("yer cekimi[9.76-9.8]=>");
				double a = ara.nextDouble();
				System.out.print("agirlik[2.2-56]=>");
				double b = ara.nextDouble();
				System.out.print("rpm=>[0-132]"); 
				double c = ara.nextDouble();
				double[] cikti2 = ysa.momentumTest(a, b, c);
				System.out.println("Output:" + cikti2[0]);
				break;
			case 9:
				System.out.println("Momentumlu Hata Oranı: "+ysa.EgitimSonMomenHata());
				break;
			case 10:
				YSA.TestOut out2 = ysa.new TestOut();
				out2=ysa.MomenTestVerisiIleSonuc();
				System.out.println("Rastgele test verisi ile test sonucu");
				System.out.println("yer cekimi:"+out2.yer);
				System.out.println("agirlik:"+out2.agirlik);
				System.out.println("rpm:"+out2.rpm);
				System.out.print("Ag sonucu: "+out2.sinirselCikti[0]+"\n");
				break;
			case 11:
				System.exit(0);
				break;
			}

		}while(secim !=11);
		ara.close();
	}

}
