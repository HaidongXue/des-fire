package edu.gsu.hxue.experimentUtilities;

import edu.gsu.hxue.gis.GISData;
import edu.gsu.hxue.gis.fuel.StandardCustomizedFuelEnum;
import edu.gsu.hxue.desFire.FireSystemConfig;
import edu.gsu.hxue.CellularAutomataPresentation;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RockHouseFireResultPresenter
{
	private CellularAutomataPresentation p;
	private final int X_DIM = 1051;
	private final int Y_DIM = 1871;

	public RockHouseFireResultPresenter()
	{
		p = new CellularAutomataPresentation(this.X_DIM, this.Y_DIM, 0.4);
	}

	private void drawIgnition(String igFile) throws FileNotFoundException
	{
		Scanner s = new Scanner(new File(igFile));
		while (s.hasNextInt())
		{
			int x = s.nextInt();
			if (!s.hasNextInt())
				break;
			int y = s.nextInt();

			p.setCellColor(x, y, Color.blue);
		}
		p.drawDirtyCellsInBuffer();
		p.showBufferOnScreen();
	}

	private void drawGISMap() throws Exception
	{
		FireSystemConfig config = new FireSystemConfig();
		String folder = config.gisFileFolder;

		System.out.println("Loading GIS map ... ");
		GISData gisMap = new GISData(folder + "rockhouse_slope.txt", // slope
				folder + "rockhouse_aspect.txt", // aspect
				folder + "rockhouse_fuel.txt" // fuel
		);

		for (int i = 0; i < gisMap.xDim(); i++)
			for (int j = 0; j < gisMap.yDim(); j++)
			{
				p.setCellColor(i, j, StandardCustomizedFuelEnum.modelIndexToFuelType(gisMap.fuelModelIndexAt(i, j)).getColor());
				p.setCellText(i, j, StandardCustomizedFuelEnum.modelIndexToFuelType(gisMap.fuelModelIndexAt(i, j)).toString());
			}
		p.drawDirtyCellsInBuffer();
		p.showBufferOnScreen();
	}
	
	private void drawEnergyMap() throws Exception
	{
		FireSystemConfig config = new FireSystemConfig();
		String folder = config.gisFileFolder;

		System.out.println("Loading GIS map ... ");
		GISData gisMap = new GISData(folder + "MooreBranch_slope.txt", // slope
				folder + "MooreBranch_aspect.txt", // aspect
				folder + "MooreBranch_fuel.txt" // fuel
		);

		for (int i = 0; i < gisMap.xDim(); i++)
			for (int j = 0; j < gisMap.yDim(); j++)
			{
				StandardCustomizedFuelEnum f = StandardCustomizedFuelEnum.modelIndexToFuelType(gisMap.fuelModelIndexAt(i, j));
				// total energy kJ
				double e = f.getFuel().totalNetFuelLoading() * f.getFuel().rothermelLowHeatContent();
				//System.out.print(e + " ");
				
				p.setCellColor(i, j, colorMappingA(e));
			}
		p.drawDirtyCellsInBuffer();
		p.showBufferOnScreen();
	}
	
	private void drawBurningTimeMap() throws Exception
	{
		FireSystemConfig config = new FireSystemConfig();
		String folder = config.gisFileFolder;

		System.out.println("Loading GIS map ... ");
		GISData gisMap = new GISData(folder + "MooreBranch_slope.txt", // slope
				folder + "MooreBranch_aspect.txt", // aspect
				folder + "MooreBranch_fuel.txt" // fuel
		);

		for (int i = 0; i < gisMap.xDim(); i++)
			for (int j = 0; j < gisMap.yDim(); j++)
			{
				StandardCustomizedFuelEnum f = StandardCustomizedFuelEnum.modelIndexToFuelType(gisMap.fuelModelIndexAt(i, j));
				// total energy kJ
				double w = f.getWeightingFactor();
				double t = 3*Math.log(10)*w/0.85141;
				
				p.setCellColor(i, j, colorMappingB(t));
			}
		p.drawDirtyCellsInBuffer();
		p.showBufferOnScreen();
	}
	private Color colorMappingA( double v )
	{
		float min = 0;
		float max = 25000;
		if(v>max)
		{
			System.out.println("!!v " + v);
			v = max;
		}
		return Color.getHSBColor(0, (float)v/max, 1);
	}
	
	private Color colorMappingB( double v )
	{
		float min = 0;
		float max = 3600;
		if(v>max)
		{
			System.out.println("!!v " + v );
			v = max;
		}
		return Color.getHSBColor((float)0.4, (float)v/max, 1);
	}
	
	private void drawColorBar()
	{
		for(int x=0; x<this.X_DIM; x++)
		{
			double e = (double)x/X_DIM * 3600;
			System.out.println(e);
			for( int y=0; y<this.Y_DIM; y++)
			{
				p.setCellColor(x, y, colorMappingB(e));
			}
		}
		p.drawDirtyCellsInBuffer();
		p.showBufferOnScreen();
	}

	private void drawContainedLine(String conFile) throws FileNotFoundException
	{
		Scanner s = new Scanner(new File(conFile));
		while (s.hasNextInt())
		{
			int x = s.nextInt();
			if (!s.hasNextInt())
				break;
			int y = s.nextInt();

			p.setCellColor(x, y, Color.orange);
		}
		p.drawDirtyCellsInBuffer();
		p.showBufferOnScreen();
	}

	private void drawRealFire(String realFile) throws FileNotFoundException
	{
		Scanner s = new Scanner(new File(realFile));
		while (s.hasNextInt())
		{
			int x = s.nextInt();
			if (!s.hasNextInt())
				break;
			int y = s.nextInt();

			p.setCellColor(x, y, Color.red);
		}
		p.drawDirtyCellsInBuffer();
		p.showBufferOnScreen();
	}

	private void drawIgnitedCells(String folder, int stepLength, int stepNumber) throws FileNotFoundException
	{
		for (int i = 0; i <= stepNumber; i++)
		{
			File f = new File(folder + Integer.toString(i * stepLength) + ".txt");
			System.out.println(f.getName());
			Scanner s = new Scanner(f);
			for (int j = 0; j < 7; j++)
			{
				s.nextLine();
			}

			while (s.hasNextLine())
			{
				if (!s.hasNextInt())
					break;
				int x = s.nextInt();
				if (!s.hasNextInt())
					break;
				int y = s.nextInt();
				for (int j = 0; j < 4; j++)
					s.next();
				p.setCellColor(x, y, Color.black);
			}
			p.drawDirtyCellsInBuffer();
			p.showBufferOnScreen();
		}
	}

	public void showResults()
	{
		try
		{
			this.p.setTitle("MB Fire");
			drawGISMap();
			
			//String folder = "C:/Users/Haydon/SkyDrive/ResearchCloud/ExperimentResults/Fire_Atmosphere_coupled simulation/HeatmapFiles_MB_1200mres/";
			//String folder = "C:/Users/Haydon/SkyDrive/ResearchCloud/ExperimentResults/Fire_Atmosphere_coupled simulation/DESFire_corrig_nospin/HeatmapFiles/";
			
			//drawIgnitedCells( folder, 60, 1440);
			
			
			//drawIgnitedCells("HeatmapFiles/", 600, 72);
			drawIgnitedCells("C:/Users/haydon/SkyDrive/ResearchCloud/ExperimentResults/RockHouseFire/HeatmapFiles_nested_1way_20130425/", 60, 360);
			
			
			//drawIgnitedCells(folder, 60, 1440);
			//drawRealFire("Day5_B.txt");
			
			//drawContainedLine("InitialContainedCells_MB_D_west.txt");
			drawIgnition("IgnitionPoints_RH_2.txt");
			
	/*		this.p.setTitle("Energy map");
			//drawEnergyMap();
			drawBurningTimeMap();
			drawRealFire("Day5_B.txt");
			//drawIgnition("IgnitionPoints_MB_corrected_spin.txt");
			//drawColorBar();
*/		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		RockHouseFireResultPresenter mb = new RockHouseFireResultPresenter();
		mb.showResults();

	}

}
