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

package edu.mda.bcb.dapi.startup;

import edu.mda.bcb.dapi.indexes.Indexes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author linux
 */
@WebListener
public class LoadIndexFiles implements ServletContextListener
{
	static public String M_INDEX_FILES = "/DAPI/INDEXES";
	static public String M_DSC_INDEX_FILES = "/DAPI/DSC_INDEXES";
	static public String M_DATA_FILES = "/DAPI/DATA";
	static public String M_CONFIG_PROP = "/DAPI/CONFIG/dapi.properties";
	static public String M_CONFIG_FILTER = "/DAPI/CONFIG/dapi-filter.tsv";
	static public String M_VERSION = "DAPI 2020-07-20-1100";

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContextListener.super.contextInitialized(sce);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Version: " + M_VERSION);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Read indexes from: " + M_INDEX_FILES);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Read DSC indexes from: " + M_DSC_INDEX_FILES);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Read data from: " + M_DATA_FILES);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Read DAPI properties from: " + M_CONFIG_PROP);
		sce.getServletContext().log("LoadIndexFiles::contextInitialized Read DAPI filter from: " + M_CONFIG_FILTER);
		// maps for rewriting options for index files
		TreeMap<String, String> baseToCategory = new TreeMap<>();
		TreeMap<String, String> baseToPlatform = new TreeMap<>();
		TreeMap<String, String> baseToDetails = new TreeMap<>();
		try
		{
			if (new File(M_CONFIG_FILTER).exists())
			{
				sce.getServletContext().log("LoadIndexFiles::contextInitialized read: " + M_CONFIG_FILTER);
				// Variant	Category	Platform	Details	Category-New	Platform-New	Details-New
				try (BufferedReader br = java.nio.file.Files.newBufferedReader(new File(M_CONFIG_FILTER).toPath(), Charset.availableCharsets().get("UTF-8")))
				{
					ArrayList<String> headersIndex = null;
					String line = br.readLine();
					while (null!=line)
					{
						if (null==headersIndex)
						{
							headersIndex = new ArrayList<>();
							for (String hdr : line.split("\t", -1))
							{
								headersIndex.add(hdr);
							}
						}
						else
						{
							String [] splitted = line.split("\t", -1);
							// Variants from real index is called Derivations here
							String valVariant = splitted[headersIndex.indexOf("Derivations")];
							String valCategory = splitted[headersIndex.indexOf("Category")];
							String valPlatform = splitted[headersIndex.indexOf("Platform")];
							String valDetails = splitted[headersIndex.indexOf("Details")];
							String valCategoryNew = splitted[headersIndex.indexOf("Category-New")];
							String valPlatformNew = splitted[headersIndex.indexOf("Platform-New")];
							String valDetailsNew = splitted[headersIndex.indexOf("Details-New")];
							String lineIndex = (valVariant + " " + valCategory + " " + valPlatform + " " + valDetails).toLowerCase();
							baseToCategory.put(lineIndex, valCategoryNew);
							baseToPlatform.put(lineIndex, valPlatformNew);
							baseToDetails.put(lineIndex, valDetailsNew);
						}
						line = br.readLine();
					}
				}
			}
			Indexes myIndexes = Indexes.loadFromFiles(M_INDEX_FILES, sce.getServletContext(), baseToCategory, baseToPlatform, baseToDetails);
			sce.getServletContext().setAttribute("INDEXES", myIndexes);
			if (new File(M_DSC_INDEX_FILES).exists())
			{
				sce.getServletContext().log("LoadIndexFiles::contextInitialized read: " + M_DSC_INDEX_FILES);
				Indexes myIndexes2 = Indexes.loadFromFiles(M_DSC_INDEX_FILES, sce.getServletContext(), baseToCategory, baseToPlatform, baseToDetails);
				sce.getServletContext().setAttribute("DSC-INDEXES", myIndexes2);
			}
			else
			{
				sce.getServletContext().log("LoadIndexFiles::contextInitialized skip reading from non-existant: " + M_DSC_INDEX_FILES);
			}
			File dapiProp = new File(M_CONFIG_PROP);
			if (dapiProp.exists())
			{
				Properties props = new Properties();
				try (FileInputStream is = new FileInputStream(dapiProp))
				{
					props.loadFromXML(is);
					sce.getServletContext().setAttribute("PROPS", props);
				}
			}
		}
		catch(Exception exp)
		{
			sce.getServletContext().log("Error loading index files", exp);
		}
	}
}
