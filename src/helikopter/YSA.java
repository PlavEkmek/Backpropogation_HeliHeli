/*
 * Uğur Çelik 27.12.2020
 * 
 */
package helikopter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class YSA {
	public YSA(Tools tl,double epoch) {
	if(!tl.ISNormalized) {System.out.println("Eğitim verisi oluşturulmamış veya normalize edilmemiş. 1-Test Hazırla ile oluşturun"); return;}
	bp = new BackPropagation();
	mbp = new MomentumBackpropagation();
	this.epoch=epoch;
	eldeEdilenHatalar = new double [(int)epoch];
	this.tl=tl;
}
	BackPropagation bp;
	MomentumBackpropagation mbp;
	Tools tl;
	private static File egitimData = new File("EgitData.txt");
	private static File testData = new File("TestData.txt");
	double epoch;
	double[] eldeEdilenHatalar;
	//Test çıktısı için döndürülecek veri paketi
	public class TestOut{
		double[] sinirselCikti;
		double yer;
		double agirlik;
		double rpm;
	}
	
	public void Egit() throws FileNotFoundException {
		
		MultiLayerPerceptron sinirselAg = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,3,3,1);
		bp.setLearningRate(0.2);
		//epoch sayisi		
		bp.setMaxError(0.01);
		sinirselAg.setLearningRule(bp);
		for(int i=0;i<epoch;i++) {
			sinirselAg.getLearningRule().doOneLearningIteration(EgitimVeriSeti());
			if(i==0) eldeEdilenHatalar[i]=1;
			else eldeEdilenHatalar[i]=sinirselAg.getLearningRule().getPreviousEpochError();
		}
		
		
		sinirselAg.save("ogrenenAg.nnet");
		System.out.println("Öğrenme Tamamlandı");
	}

	
	
	public DataSet EgitimVeriSeti() throws FileNotFoundException {
		Scanner oku = new Scanner(egitimData);
		DataSet egitim = new DataSet(3,1); 
		while(oku.hasNextDouble())
		{
			egitim.add(new DataSetRow(new double[] {oku.nextDouble(),oku.nextDouble(),oku.nextDouble()},new double[] {oku.nextInt()}));
		}
		oku.close();
		return egitim;
	}
	
	public double[] test (double x, double y,double z)
	{
		NeuralNetwork  sinirselAg = new NeuralNetwork().createFromFile("ogrenenAg.nnet");
		sinirselAg.setInput(x,y,z);
		sinirselAg.calculate();
		return sinirselAg.getOutput();
	}
	public TestOut TestVerisiIleSonuc() throws FileNotFoundException {
		Scanner ara = new Scanner(testData);
		NeuralNetwork  sinirselAg = new NeuralNetwork().createFromFile("ogrenenAg.nnet");
		ArrayList<Double> yer = new ArrayList<Double>();
		ArrayList<Double> agirlik = new ArrayList<Double>();
		ArrayList<Double> rpm = new ArrayList<Double>();
		
		while(ara.hasNextDouble())
		{
			yer.add(ara.nextDouble());
			agirlik.add(ara.nextDouble());
			rpm.add(ara.nextDouble());
			//Output versinin atlanması için
			ara.nextInt();
		}
		ara.close();
		Random rnd = new Random();
		
		TestOut out = new TestOut();
		
		int index = rnd.nextInt(300);
		out.yer=yer.get(index)*(tl.MaxYer-tl.MinYer)+tl.MinYer;
		out.agirlik=agirlik.get(index)*(tl.MaxAgir-tl.MinAgir)+tl.MinAgir;
		out.rpm=rpm.get(index)*(tl.MaxRpm-tl.MinRpm)+tl.MinRpm;

		sinirselAg.setInput(out.yer,out.agirlik,out.rpm);
		sinirselAg.calculate();
		out.sinirselCikti=sinirselAg.getOutput();
		return out;
		
	}
	public double[] Hatalar() {
		
		return eldeEdilenHatalar;
	}
	public double EgitimSonHata() {
		
		return bp.getTotalNetworkError();
	}
	
	
	public void MomentumEgit() throws FileNotFoundException {
		MultiLayerPerceptron sinirselAg = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,3,4,2,1);
		mbp.setLearningRate(0.2);
		mbp.setMomentum(0.8);
		//epoch sayisi
		mbp.setMaxIterations(8000);
		sinirselAg.setLearningRule(mbp);
		sinirselAg.learn(EgitimVeriSeti());
		sinirselAg.save("momentumOgrenenAg.nnet");
		System.out.println("Momentumlu Ogrenme Tamamlandi");
	}

	
	public double[] momentumTest (double x, double y,double z)
	{
		NeuralNetwork  sinirselAg = new NeuralNetwork().createFromFile("momentumOgrenenAg.nnet");
		sinirselAg.setInput(x,y,z);
		sinirselAg.calculate();
		return sinirselAg.getOutput();
	}
	public double EgitimSonMomenHata() {
		return mbp.getTotalNetworkError();
	}
	public TestOut MomenTestVerisiIleSonuc() throws FileNotFoundException {
		Scanner ara = new Scanner(testData);
		NeuralNetwork  sinirselAg = new NeuralNetwork().createFromFile("momentumOgrenenAg.nnet");
		ArrayList<Double> yer = new ArrayList<Double>();
		ArrayList<Double> agirlik = new ArrayList<Double>();
		ArrayList<Double> rpm = new ArrayList<Double>();
		
		while(ara.hasNextDouble())
		{
			yer.add(ara.nextDouble());
			agirlik.add(ara.nextDouble());
			rpm.add(ara.nextDouble());
			//Output versinin atlanması için
			ara.nextInt();
		}
		ara.close();
		Random rnd = new Random();
		
		TestOut out = new TestOut();
		
		int index = rnd.nextInt(300);
		out.yer=yer.get(index)*(tl.MaxYer-tl.MinYer)+tl.MinYer;
		out.agirlik=agirlik.get(index)*(tl.MaxAgir-tl.MinAgir)+tl.MinAgir;
		out.rpm=rpm.get(index)*(tl.MaxRpm-tl.MinRpm)+tl.MinRpm;

		sinirselAg.setInput(out.yer,out.agirlik,out.rpm);
		sinirselAg.calculate();
		out.sinirselCikti=sinirselAg.getOutput();
		return out;
		
	}
}
