/*
 *  Copyright (c) 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020 University of Texas MD Anderson Cancer Center
 *  
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  MD Anderson Cancer Center Bioinformatics on GitHub <https://github.com/MD-Anderson-Bioinformatics>
 *  MD Anderson Cancer Center Bioinformatics at MDA <https://www.mdanderson.org/research/departments-labs-institutes/departments-divisions/bioinformatics-and-computational-biology.html>

 */
package edu.mda.bcb.bev.indexes;

import edu.mda.bcb.bev.query.Dataset;
import edu.mda.bcb.bev.startup.LoadIndexFiles;
import static edu.mda.bcb.bev.startup.LoadIndexFiles.M_DATA_FILES;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Tod-Casasent
 */
public class IndexesBei extends Indexes
{
	public IndexesBei()
	{
		super();
	}

	@Override
	synchronized public Dataset getDataset(String theId)
	{
		//String zipPath = new File(LoadIndexFiles.M_DATA_FILES, theId + ".zip").getAbsolutePath();
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public File getZipPath(String theId)
	{
		return new File(LoadIndexFiles.M_DATA_FILES, theId + ".zip");
	}

	@Override
	synchronized public Collection<Dataset> getDatasets()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public TreeSet<String> getNewestIds()
	{
		TreeSet<String> ids = new TreeSet<>();
		File[] indexes = new File(M_DATA_FILES).listFiles();
		for (File zip : indexes)
		{
			String source = zip.getName().split(".", -1)[0];
			ids.add(source);
		}
		return ids;
	}

	@Override
	synchronized public Set<String> getIds()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysFiles()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysSource()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysVariant()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysProject()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysSubProject()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysCategory()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysPlatform()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysData()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysAlgorithm()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysDetails()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public Set<String> getKeysVersion()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	synchronized public TreeSet<Dataset> query(ArrayList<String> theFiles, ArrayList<String> theSources, ArrayList<String> theVariants, ArrayList<String> theProjects, ArrayList<String> theSubprojects, ArrayList<String> theCategories, ArrayList<String> thePlatforms, ArrayList<String> theData, ArrayList<String> theAlgorithms, ArrayList<String> theDetails, ArrayList<String> theVersions)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
