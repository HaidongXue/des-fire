package edu.gsu.hxue.physicalModels.rothermel.andreasBachman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

//************************************************************************
/* BehaveTest */

/**
   Class to run Behave

   @author andreas bachmann
   @version 1.0 april 2001
   ----------------------------------------------------------------------

   get your own copy from:
          http://www.geo.unizh.ch/gis/research/edmg/fire/unc.html
   send comments to:
          bachmann@geo.unizh.ch

   or by letter:
          University of Zurich
          Deptartment of Geography
          Geographic Information Systems Division
          Winterthurerstr. 190
          8057 Zurich
          Switzerland
*/
//************************************************************************

public class BehaveTest{

  public static void main(String[] args){
    // without properties-file: print usage and exit...
    if (args.length<1) {
      //usage();
      //System.exit(1);
    }
    //JFileChooser jf = new JFileChooser("c:/Lewis/ECE5775/DEVSJAVA/OldDevsFiles/src/wf/data");
    //jf.enable();
    //String filename = args[0];
    //String filename = jf.getSelectedFile();
    //File file = jf.getSelectedFile();
    //String filename = file.getName();
    //System.out.println("Reading file " + filename);
    String filename = "c:/Lewis/ECE575/DEVSJAVA/OldDevsFiles/src/wf/data/nffl5.prp";

    Behave b = new Behave();
    Properties p;
    //
    p = new Properties();
    try {
      //p.load(new FileInputStream(filename));
      p.load(new FileInputStream(filename));
    }catch (FileNotFoundException e) {
      System.out.println("File "+filename+" not found.");
    }catch (IOException e) {
      e.printStackTrace();
    }

    // parse the values...
    String key = " ";
    String value;
    StringTokenizer propValString;
    StringTokenizer fieldTag;

    for (Iterator i=p.keySet().iterator(); i.hasNext(); ) {
      try {
        key   = (String) i.next();
        // get the value for the corresponding key
        propValString = new StringTokenizer(p.getProperty(key));
        value         = propValString.nextToken();
        b.setParameterMean(key, Double.parseDouble(value));
        //
      }catch(NoSuchElementException e) {
        System.out.println("no such key! " + key);
      }
    }
    /////////////////////////////////////////////////////////////
    // This part added by Lewis Ntaimo December 6, 2002
    // alter the wind speed and direction
    b.wsp = 11;
    b.wdr = 180;
    ///////////////////////////////////


    //
    // now calculate fire behaviour
    b.calc();
    //
    // print out inputs and outputs...
    DecimalFormat df1 = new DecimalFormat("#,##0.0");
    DecimalFormat df2 = new DecimalFormat("#,##0.00");
    DecimalFormat df3 = new DecimalFormat("#,##0.000");
    DecimalFormat df4 = new DecimalFormat("#,##0.0000");
    DecimalFormat df5 = new DecimalFormat("#,##0.00000");

    show(" ");
    show("  Effective mineral content    s_e          [%] =" +
         lPad(df1.format(b.s_e  ),10));
    show("  Total mineral content        s_t          [%] =" +
         lPad(df1.format(b.s_t  ),10));
    show("  Heat content                 heat     [kJ/kg] =" +
         lPad(df2.format(b.heat ),10));
    show("  Particle density             rho_p    [kg/m3] =" +
         lPad(df2.format(b.rho_p),10));
    show("  Fuel bed depth               d            [m] =" +
         lPad(df2.format(b.depth),10));
    show("  Moisture of extinction       mx           [%] =" +
         lPad(df2.format(b.mx   ),10));
    show(" ");
    // fuel properties
    show("  Size |  Fuel Load | Surface-to-Volume- | Moisture");
    show("       |    [kg/m2] | Ratio        [1/m] |      [%]");
    show(" ---------------------------------------------------");
    show("    d1 |" +
         lPad(df3.format(b.w0_d1),10) + "  |" +
         lPad(df3.format(b.sv_d1),18) + "  |" +
         lPad(df1.format(b.m_d1 ), 8));
    show("    d2 |" +
         lPad(df3.format(b.w0_d2),10) + "  |" +
         lPad(df3.format(b.sv_d2),18) + "  |" +
         lPad(df1.format(b.m_d2) , 8));
    show("    d3 |" +
         lPad(df3.format(b.w0_d3),10) + "  |" +
         lPad(df3.format(b.sv_d3),18) + "  |" +
         lPad(df1.format(b.m_d3 ), 8));
    show("    lh |" +
         lPad(df3.format(b.w0_lh),10) + "  |" +
         lPad(df3.format(b.sv_lh),18) + "  |" +
         lPad(df1.format(b.m_lh),  8));
    show("    lw |" +
         lPad(df3.format(b.w0_lw),10)+ "  |" +
         lPad(df3.format(b.sv_lw),18)+ "  |" +
         lPad(df1.format(b.m_lw ), 8));
    //
    show(" ");
    show("  Wind                         |  Terrain");
    show(" ------------------------------------------------------");
    show("   wind speed     [m/s]" + lPad(df2.format(b.wsp),7) +
         " |   slope  [deg]"       + lPad(df2.format(b.slp),7));
    show("   wind direction [deg]" + lPad(df2.format(b.wdr),7) +
         " |   aspect [deg]"       + lPad(df2.format(b.asp),7));
    show(" ");
    // results
    show("  => Characteristic sv-ratio   sigma      [1/m] =" +
         lPad(df2.format(b.sigma),10));
    show("  => Mean bulk density         rho_b    [kg/m3] =" +
         lPad(df4.format(b.rho_b),12));
    show("  => Packing ratio             beta         [-] =" +
         lPad(df5.format(b.beta ),13));
    show("  => Optimal packing ratio     beta_opt     [-] =" +
         lPad(df5.format(b.beta_opt),13));
    show(" ");
    show("  => Rate of spread            ros        [m/s] =" +
         lPad(df4.format(b.ros),12));
    show("  => Heat sink                 hsk      [kJ/m3] =" +
         lPad(df2.format(b.hsk),10));
    show("  => Reaction intensity        I_r      [kW/m2] =" +
         lPad(df2.format(b.I_r),10));
    show("  => Propagating flux ratio    xi           [-] =" +
         lPad(df4.format(b.xi),12));
    show(" ");
    show("  => Wind and slope factor     phi_t        [-] =" +
         lPad(df4.format(b.phi_t),12));
    show("  => Direction of max. spread  sdr        [deg] =" +
         lPad(df1.format(b.sdr),9));
    show("  => Effective Wind speed      efw        [m/s] =" +
         lPad(df2.format(b.efw),10));
    show(" ");
    show("  => Flame length              fln          [m] =" +
         lPad(df2.format(b.fln),10));
    show("  => Fire line intensity       fli       [kW/m] =" +
         lPad(df2.format(b.fli),10));
    show("  => Heat per area             hpa      [kJ/m2] =" +
         lPad(df2.format(b.hpa),10));
    show("  => flame residence time      tau          [s] =" +
         lPad(df2.format(b.tau),10));
    show("  => flame zone depth          fzd          [m] =" +
         lPad(df3.format(b.fzd),11));
  }

  public static void usage() {
    show("Usage: java BehaveTest input_file");
  }

  public static void show(String text) {
    System.out.println(text);
  }

  public static String lPad(String t, int outputWidth) {
    while (t.length() < outputWidth) {
      t =" "+t;
    }
    return t;
  }

}
