/*
 *  Copyright (c) 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021 University of Texas MD Anderson Cancer Center
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
package edu.mda.bcb.bev.query;

import edu.mda.bcb.bev.indexes.Indexes;
import java.util.TreeSet;

/**
 *
 * @author Tod-Casasent
 */
public class Result
{
	public OptionKeys mOptions;
	public TreeSet<Dataset> mDatasets;
	
	public Result()
	{
		mOptions = new OptionKeys();
		mDatasets = new TreeSet<>();
	}
	
	public void init(Indexes theIndex)
	{
		mOptions.mFiles.addAll(theIndex.getKeysFiles());
		mOptions.mSource.addAll(theIndex.getKeysSource());
		mOptions.mVariant.addAll(theIndex.getKeysVariant());
		mOptions.mProject.addAll(theIndex.getKeysProject());
		mOptions.mSubproject.addAll(theIndex.getKeysSubProject());
		mOptions.mCategory.addAll(theIndex.getKeysCategory());
		mOptions.mPlatform.addAll(theIndex.getKeysPlatform());
		mOptions.mData.addAll(theIndex.getKeysData());
		mOptions.mAlgorithm.addAll(theIndex.getKeysAlgorithm());
		mOptions.mDetail.addAll(theIndex.getKeysDetails());
		mOptions.mVersion.addAll(theIndex.getKeysVersion());
		mOptions.mOverallDSCpvalue.addAll(theIndex.getKeysOverallDSCpvalue());
	}
}
