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

package edu.mda.bcb.bev.query;

import edu.mda.bcb.bev.indexes.Indexes;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author linux
 */
public class Query
{
	public ArrayList<String> mFiles;
	public ArrayList<String> mSources;
	public ArrayList<String> mVariants;
	public ArrayList<String> mProjects;
	public ArrayList<String> mSubprojects;
	public ArrayList<String> mCategories;
	public ArrayList<String> mPlatforms;
	public ArrayList<String> mData;
	public ArrayList<String> mAlgorithms;
	public ArrayList<String> mDetails;
	public ArrayList<String> mVersions;

	public Query(ArrayList<String> theFiles, ArrayList<String> theSources, ArrayList<String> theVariants, 
			ArrayList<String> theProjects, ArrayList<String> theSubprojects, ArrayList<String> theCategories, 
			ArrayList<String> thePlatforms, ArrayList<String> theData, ArrayList<String> theAlgorithms, 
			ArrayList<String> theDetails, ArrayList<String> theVersions)
	{
		this.mFiles = theFiles;
		this.mSources = theSources;
		this.mVariants = theVariants;
		this.mProjects = theProjects;
		this.mSubprojects = theSubprojects;
		this.mCategories = theCategories;
		this.mPlatforms = thePlatforms;
		this.mData = theData;
		this.mAlgorithms = theAlgorithms;
		this.mDetails = theDetails;
		this.mVersions = theVersions;
	}
	
	public TreeSet<Dataset> process(Indexes theIndexes)
	{
		return theIndexes.query(mFiles, mSources, mVariants, mProjects, mSubprojects, mCategories, mPlatforms, mData, mAlgorithms, mDetails, mVersions);
	}
}
