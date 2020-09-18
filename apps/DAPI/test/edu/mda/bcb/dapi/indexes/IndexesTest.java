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

package edu.mda.bcb.dapi.indexes;

import edu.mda.bcb.dapi.startup.LoadIndexFiles;
import java.io.File;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import org.junit.Test;

/**
 *
 * @author Tod-Casasent
 */
public class IndexesTest
{
	
	public IndexesTest()
	{
	}

	public void testIndex(Indexes theIndexes)
	{
		printSet("Files", theIndexes.getKeysFiles());
		printSet("Source", theIndexes.getKeysSource());
		printSet("Variant", theIndexes.getKeysVariant());
		printSet("Project", theIndexes.getKeysProject());
		printSet("SubProject", theIndexes.getKeysSubProject());
		printSet("Category", theIndexes.getKeysCategory());
		printSet("Platform", theIndexes.getKeysPlatform());
		printSet("Data", theIndexes.getKeysData());
		printSet("Algorithm", theIndexes.getKeysAlgorithm());
		printSet("Details", theIndexes.getKeysDetails());
		printSet("Version", theIndexes.getKeysVersion());
		System.out.println("Ids n=" + theIndexes.getIds().size());
		System.out.println("Datasets n=" + theIndexes.getDatasets().size());
		System.out.println("Newest IDs n=" + theIndexes.getNewestIds().size());
	}
	
	protected void printSet(String theTitle, Set<String> theSet)
	{
		System.out.println("====================");
		System.out.println("= " + theTitle + " n=" + theSet.size());
		for (String key : theSet)
		{
			System.out.println("\t" + key);
		}
		System.out.println("====================");
	}
	
	/**
	 * Test of loadIndex method, of class Indexes.
	 */
	@Test
	public void testLoadIndex() throws Exception
	{
		System.out.println("loadIndex");
		File theIndexFile = new File("../../data/testing_static/DAPI/INDEXES/gdc_index.tsv");
		String theIndexSource = "gdc";
		Indexes instance = new Indexes();
		instance.loadIndex(theIndexFile, theIndexSource, new TreeMap<String, String>(), new TreeMap<String, String>(), new TreeMap<String, String>());
		testIndex(instance);
	}

	/**
	 * Test of loadFromFiles method, of class Indexes.
	 */
	@Test
	public void testLoadFromFiles()
	{
		System.out.println("loadFromFiles");
		ServletContext theSC = null;
		LoadIndexFiles.M_INDEX_FILES = "../../data/testing_static/DAPI/INDEXES";
		Indexes instance = Indexes.loadFromFiles(LoadIndexFiles.M_INDEX_FILES, theSC, new TreeMap<String, String>(), new TreeMap<String, String>(), new TreeMap<String, String>());
		testIndex(instance);
	}
	
}
