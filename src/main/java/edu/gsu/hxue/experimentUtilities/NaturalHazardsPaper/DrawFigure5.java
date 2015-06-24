package edu.gsu.hxue.experimentUtilities.NaturalHazardsPaper;

import edu.gsu.hxue.experimentUtilities.heatfilePresenter.HeatfilePresentationLogic;

import java.awt.*;
import java.io.File;

public class DrawFigure5
{

	public static void main(String[] args)
	{
		showSymResults();
	}
	
	private static void showSymResults()
	{
		int endTime = 1800;
		
		int[] us = {3}; // wind speeds
		int[] fireResSet = {30}; // 10, 30, 90
		int[] arpsResSet = {90}; // 10, 30, 90
		
		String folder = "C:/Users/Haydon/Google Drive/DEVSFIRE_ARPS Coupling/IJWF Revision/Simulation data/figure5data/HeatmapFiles_IJWFv4/";
		for (int arpsRes : arpsResSet)
			for (int fireRes : fireResSet)
				for (int u : us)
				{
					if(fireRes > arpsRes) continue;
					
					//if(fireRes==30 && arpsRes==90) continue;
					
					String fuelPath = folder+"settingFiles/fuel_sym_" + String.format("%d", fireRes) + ".txt";
					String slopePath = folder+"settingFiles/slope_sym_" + String.format("%d", fireRes) + ".txt";
					String aspectPath = folder+"settingFiles/aspect_sym_" + String.format("%d", fireRes) + ".txt"; 
					File initialIgnitionFile = new File(folder+"settingFiles/IgnitionPoints_sym_" + String.format("%d", fireRes) + "m.txt");
					File delayedIgnitionFile = new File(folder+"settingFiles/empty.txt");
					File initialContainedFile = new File(folder+"settingFiles/empty.txt");
					
					try
					{
						
						int stepLength = 60;
						int startStep = 0;
						int endStep = endTime/stepLength;
						String name = String.format("arpsRes=%dm; devsfireRes=%dm; iniWind=%dm/s at %ds ", arpsRes, fireRes, u, endTime);
						
						double scalar = 2.5;
						double presentationCellSize = 30;
						HeatfilePresentationLogic p = new HeatfilePresentationLogic(fuelPath, slopePath, aspectPath, initialIgnitionFile, delayedIgnitionFile, initialContainedFile, name, presentationCellSize, scalar );
						//p.drawGIS();
						//p.drawIgnition();
						//p.drawDelayedIgnition();
						//p.drawContainedCells();
						
						{
							
							String heatfolder = folder + "HeatmapFiles_"+ String.format("%d%d_u%d", arpsRes, fireRes, u)+"_uncoupled/";
							p.drawHeatfilesOutline(heatfolder, stepLength, startStep, endStep, Color.black);
							System.out.println(name+ "uncoupled done" );
						}
						
						{
							String heatfolder = folder + "HeatmapFiles_"+ String.format("%d%d_u%d", arpsRes, fireRes, u)+"_up60/";
							//String folder = "C:/Users/Haydon/Desktop/heatfileDebug/HeatmapFiles_symdebug"+ String.format("%d%d/", arpsRes, fireRes);
							p.drawHeatfilesOutline(heatfolder, stepLength, startStep, endStep, Color.red);
							System.out.println(name+ "coupled done" );
						}
						
						//System.out.println("drew heat map");
						
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

	}
	
	private static void test()
	{
		String fuelPath = "C:/Users/Haydon/Desktop/HeatmapFiles_symtests/settingFiles/fuel_sym_10.txt";
		String slopePath = "C:/Users/Haydon/Desktop/HeatmapFiles_symtests/settingFiles/slope_sym_10.txt";
		String aspectPath = "C:/Users/Haydon/Desktop/HeatmapFiles_symtests/settingFiles/aspect_sym_10.txt"; 
		File initialIgnitionFile = new File("C:/Users/Haydon/Desktop/HeatmapFiles_symtests/settingFiles/IgnitionPoints_sym_10m.txt");
		File delayedIgnitionFile = new File("C:/Users/Haydon/Desktop/HeatmapFiles_symtests/settingFiles/empty.txt");
		File initialContainedFile = new File("C:/Users/Haydon/Desktop/HeatmapFiles_symtests/settingFiles/empty.txt");
		
		try
		{
			int stepLength = 2;
			int startStep = 0;
			int endStep = 50;
			
			HeatfilePresentationLogic p = new HeatfilePresentationLogic(fuelPath, slopePath, aspectPath, initialIgnitionFile, delayedIgnitionFile, initialContainedFile, String.format("at %d", stepLength*endStep), 1.5 );
			//p.drawGIS();
			//p.drawIgnition();
			//p.drawDelayedIgnition();
			//p.drawContainedCells();
			
			
			{
				String folder = "C:/Users/Haydon/Desktop/HeatmapFiles_symtests/HeatmapFiles_9010_u20_coupled/";
				
				p.drawHeatfilesOutline(folder, stepLength, startStep, endStep, Color.red);
			}
			{
				String folder = "C:/Users/Haydon/Desktop/HeatmapFiles_symtests/HeatmapFiles_9010_u20_uncoupled/";
				p.drawHeatfilesOutline(folder, stepLength, startStep, endStep, Color.black);
			}
			System.out.println("drew heat map");
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
