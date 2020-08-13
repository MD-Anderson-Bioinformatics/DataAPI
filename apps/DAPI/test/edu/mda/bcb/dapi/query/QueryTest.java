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

package edu.mda.bcb.dapi.query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.mda.bcb.dapi.indexes.Indexes;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author linux
 */
public class QueryTest
{
	static public Indexes M_INSTANCE = null;
	
	public QueryTest()
	{
	}
	
	@Before
	public void setUp()
	{
		try
		{
			System.out.println("loadIndex");
			File theIndexFile = new File("../../data/testing_static/DAPI/INDEXES/gdc_index.tsv");
			String theIndexSource = "gdc";
			M_INSTANCE = new Indexes();
			M_INSTANCE.loadIndex(theIndexFile, theIndexSource, new TreeMap<String, String>(), new TreeMap<String, String>(), new TreeMap<String, String>());
		}
		catch(Exception exp)
		{
			System.err.println("Error in setUp()");
			exp.printStackTrace(System.err);
		}
	}
	
	/**
	 * Test of process method, of class Query.
	 */
	@Test
	public void testWithBatchInfo()
	{
		System.out.println("testWithBatchInfo");
		ArrayList<String> theFiles = new ArrayList<String>(Arrays.asList("batches.tsv"));
		ArrayList<String> theSources = new ArrayList<>();
		ArrayList<String> theVariants = new ArrayList<>();
		ArrayList<String> theProjects = new ArrayList<>();
		ArrayList<String> theSubprojects = new ArrayList<>();
		ArrayList<String> theCategories = new ArrayList<>();
		ArrayList<String> thePlatforms = new ArrayList<>();
		ArrayList<String> theData = new ArrayList<>();
		ArrayList<String> theAlgorithms = new ArrayList<>();
		ArrayList<String> theDetails = new ArrayList<>();
		ArrayList<String> theVersions = new ArrayList<>();
		Query instance = new Query(theFiles, theSources, theVariants, theProjects, theSubprojects, 
				theCategories, thePlatforms, theData, theAlgorithms, theDetails, theVersions, null, null, null);
		
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		System.out.println("JSON(instance)=" + gson.toJson(instance));
		
		TreeSet<Dataset> result = instance.process(M_INSTANCE);
		System.out.println("Datasets with batch files n=" + result.size());
	}
	
	@Test
	public void testMethylWithBatchInfo()
	{
		System.out.println("testMethylWithBatchInfo");
		ArrayList<String> theFiles = new ArrayList<String>(Arrays.asList("batches.tsv"));
		ArrayList<String> theSources = new ArrayList<>();
		ArrayList<String> theVariants = new ArrayList<>();
		ArrayList<String> theProjects = new ArrayList<>();
		ArrayList<String> theSubprojects = new ArrayList<>();
		ArrayList<String> theCategories = new ArrayList<>(Arrays.asList("Methylation Beta Value"));
		ArrayList<String> thePlatforms = new ArrayList<>();
		ArrayList<String> theData = new ArrayList<>();
		ArrayList<String> theAlgorithms = new ArrayList<>();
		ArrayList<String> theDetails = new ArrayList<>();
		ArrayList<String> theVersions = new ArrayList<>();
		Query instance = new Query(theFiles, theSources, theVariants, theProjects, theSubprojects, theCategories, 
				thePlatforms, theData, theAlgorithms, theDetails, theVersions, null, null, null);
		TreeSet<Dataset> result = instance.process(M_INSTANCE);
		System.out.println("Methylation datasets with batch files n=" + result.size());
	}
	
	
	@Test
	public void testCOREWithBatchInfo()
	{
		System.out.println("testCOREWithBatchInfo");
		ArrayList<String> theFiles = new ArrayList<String>(Arrays.asList("batches.tsv"));
		ArrayList<String> theSources = new ArrayList<>();
		ArrayList<String> theVariants = new ArrayList<>();
		ArrayList<String> theProjects = new ArrayList<>();
		ArrayList<String> theSubprojects = new ArrayList<>(Arrays.asList("TCGA-COAD", "TCGA-READ"));
		ArrayList<String> theCategories = new ArrayList<>();
		ArrayList<String> thePlatforms = new ArrayList<>();
		ArrayList<String> theData = new ArrayList<>();
		ArrayList<String> theAlgorithms = new ArrayList<>();
		ArrayList<String> theDetails = new ArrayList<>();
		ArrayList<String> theVersions = new ArrayList<>();
		Query instance = new Query(theFiles, theSources, theVariants, theProjects, theSubprojects, theCategories, 
				thePlatforms, theData, theAlgorithms, theDetails, theVersions, null, null, null);
		TreeSet<Dataset> result = instance.process(M_INSTANCE);
		System.out.println("Methylation datasets with batch files n=" + result.size());
	}
}
