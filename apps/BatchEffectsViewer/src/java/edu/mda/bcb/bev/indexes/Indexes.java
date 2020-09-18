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

package edu.mda.bcb.bev.indexes;

import edu.mda.bcb.bev.query.Dataset;
import static edu.mda.bcb.bev.startup.LoadIndexFiles.M_INDEX_FILES;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.ServletContext;

/**
 *
 * @author Tod-Casasent
 */
abstract public class Indexes
{
	public Indexes()
	{
	}

	synchronized static public Indexes loadFromFiles(ServletContext theSC)
	{
		Indexes retIndexes = null;
		if (new File(M_INDEX_FILES).exists())
		{
			IndexesSdb myIndexes = new IndexesSdb();
			retIndexes = myIndexes;
			File[] indexes = new File(M_INDEX_FILES).listFiles();
			for (File ind : indexes)
			{
				String source = ind.getName().split("_", -1)[0];
				try
				{
					theSC.log("Indexes::loadFromFiles Load " + ind.getAbsolutePath());
					myIndexes.loadIndex(ind, source);
				}
				catch (Exception exp)
				{
					if (null != theSC)
					{
						theSC.log("Error loading index " + ind.getAbsolutePath(), exp);
					}
					else
					{
						System.err.println("Error loading index " + ind.getAbsolutePath());
						exp.printStackTrace(System.err);
					}
				}
			}
		}
		else
		{
			IndexesBei myIndexes = new IndexesBei();
			retIndexes = myIndexes;
		}
		return retIndexes;
	}

	abstract public Dataset getDataset(String theId);

	abstract public File getZipPath(String theId);

	abstract public Collection<Dataset> getDatasets();

	abstract public TreeSet<String> getNewestIds();

	abstract public Set<String> getIds();

	abstract public Set<String> getKeysFiles();

	abstract public Set<String> getKeysSource();

	abstract public Set<String> getKeysVariant();

	abstract public Set<String> getKeysProject();

	abstract public Set<String> getKeysSubProject();

	abstract public Set<String> getKeysCategory();

	abstract public Set<String> getKeysPlatform();

	abstract public Set<String> getKeysData();

	abstract public Set<String> getKeysAlgorithm();

	abstract public Set<String> getKeysDetails();

	abstract public Set<String> getKeysVersion();
	
	abstract public TreeSet<Dataset> query(ArrayList<String> theFiles, ArrayList<String> theSources, ArrayList<String> theVariants,
			ArrayList<String> theProjects, ArrayList<String> theSubprojects, ArrayList<String> theCategories,
			ArrayList<String> thePlatforms, ArrayList<String> theData, ArrayList<String> theAlgorithms,
			ArrayList<String> theDetails, ArrayList<String> theVersions);
}
