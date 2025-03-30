/*
 This file is part of the STREAM Graphical User Interface program (STREAM GUI).

 STREAM GUI is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as
 published by the Free Software Foundation, either version 3 of the License,
 or (at your option) any later version.

 The STREAM GUI software is distributed in the hope that it will be
 useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with the software.  If not, see <http://www.gnu.org/licenses/>.
*/
package GUI;

import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author Jake
 */
public class LicenseManager {
    
    public static void SetAgreed(boolean aAgreed)
    {
        mAgreed = aAgreed;
    }
    
    public static boolean GetAgreed()
    {
        return mAgreed;
    }
    
    public static boolean LicenseFileExists()
    {
        boolean lFound = false;
        
        File f = new File(sLicFileName);
        if(f.exists() && !f.isDirectory())
        { 
            System.out.print("License file found.");
//            System.out.println(f.getAbsolutePath());
            lFound = true;
        }
        
        return lFound;
    }
    
    public static void WriteLicenseFile(String aLicenseText)
    {
        try
        {
            PrintWriter writer = new PrintWriter(sLicFileName, "UTF-8");

            writer.println(aLicenseText);
            writer.close();
        }
        catch(Exception lEx)
        {
            lEx.printStackTrace();
        }
    }
    
    private static boolean mAgreed = false;
    private static final String sLicFileName = "EULA_File.txt";
}
