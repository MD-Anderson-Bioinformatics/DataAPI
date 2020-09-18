// Copyright (c) 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020 University of Texas MD Anderson Cancer Center
//
// This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// MD Anderson Cancer Center Bioinformatics on GitHub <https://github.com/MD-Anderson-Bioinformatics>
// MD Anderson Cancer Center Bioinformatics at MDA <https://www.mdanderson.org/research/departments-labs-institutes/departments-divisions/bioinformatics-and-computational-biology.html>

package edu.mda.bcb.bev.startup;

import edu.mda.bcb.bev.indexes.Indexes;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author Tod-Casasent
 */
@WebListener
public class LoadIndexFiles implements ServletContextListener
{
	static public String M_INDEX_FILES = "/BEV/INDEXES";
	static public String M_DATA_FILES = "/BEV/DATA";
	static public String M_CONFIG_FILES = "/DAPI/CONFIG/dapi.properties";
	static public String M_VERSION = "BEV 2020-09-11-1000";

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContextListener.super.contextInitialized(sce);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Version: " + M_VERSION);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Read indexes from: " + M_INDEX_FILES);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Read data from: " + M_DATA_FILES);
		Indexes myIndexes = Indexes.loadFromFiles(sce.getServletContext());
		sce.getServletContext().setAttribute("INDEXES", myIndexes);
		try
		{
			File dapiProp = new File(M_CONFIG_FILES);
			if (dapiProp.exists())
			{
				sce.getServletContext().log("LoadIndexFiles::contextInitialized dapiProp loading " + M_CONFIG_FILES);
				Properties props = new Properties();
				try (FileInputStream is = new FileInputStream(dapiProp))
				{
					props.loadFromXML(is);
					sce.getServletContext().setAttribute("PROPS", props);
				}
			}
			else
			{
				sce.getServletContext().log("LoadIndexFiles::contextInitialized dapiProp not found " + M_CONFIG_FILES);
			}
		}
		catch(Exception exp)
		{
			sce.getServletContext().log("Error loading " + M_CONFIG_FILES, exp);
		}
	}
}
