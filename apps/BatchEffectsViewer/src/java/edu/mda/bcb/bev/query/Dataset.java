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

import java.util.ArrayList;

/**
 *
 * @author linux
 */
public class Dataset implements Comparable<Dataset>
{
	public String mIndexSource;
	public String mID;
	public ArrayList<String> mFiles;
	public String mSource;
	public String mVariant;
	public String mProject;
	public String mSubproject;
	public String mCategory;
	public String mPlatform;
	public String mData;
	public String mAlgorithm;
	public String mDetail;
	public String mVersion;
	public String mPath;

	public Dataset(String theIndexSource, String theID, ArrayList<String> theFiles, String theSource, String theVariant, String theProject, String theSubproject, 
			String theCategory, String thePlatform, String theData, String theAlgorithm, String theDetail, String theVersion, String thePath)
	{
		this.mIndexSource = theIndexSource;
		this.mID = theID;
		this.mFiles = theFiles;
		this.mSource = theSource;
		this.mVariant = theVariant;
		this.mProject = theProject;
		this.mSubproject = theSubproject;
		this.mCategory = theCategory;
		this.mPlatform = thePlatform;
		this.mData = theData;
		this.mAlgorithm = theAlgorithm;
		this.mDetail = theDetail;
		this.mVersion = theVersion;
		this.mPath = thePath;
	}

	@Override
	public int compareTo(Dataset o)
	{
		int comp = this.mSource.compareTo(o.mSource);
		if (0==comp)
		{
			comp = this.mVariant.compareTo(o.mVariant);
			if (0==comp)
			{
				comp = this.mProject.compareTo(o.mProject);
				if (0==comp)
				{
					comp = this.mSubproject.compareTo(o.mSubproject);
					if (0==comp)
					{
						comp = this.mCategory.compareTo(o.mCategory);
						if (0==comp)
						{
							comp = this.mPlatform.compareTo(o.mPlatform);
							if (0==comp)
							{
								comp = this.mData.compareTo(o.mData);
								if (0==comp)
								{
									comp = this.mAlgorithm.compareTo(o.mAlgorithm);
									if (0==comp)
									{
										comp = this.mDetail.compareTo(o.mDetail);
										if (0==comp)
										{
											comp = this.mVersion.compareTo(o.mVersion);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return comp;
	}
	
	public boolean isSameExceptForVersion(Dataset theDS)
	{
		boolean same = false;
		if ((this.mSource.equals(theDS.mSource)) &&
				(this.mVariant.equals(theDS.mVariant)) &&
				(this.mProject.equals(theDS.mProject)) &&
				(this.mSubproject.equals(theDS.mSubproject)) &&
				(this.mCategory.equals(theDS.mCategory)) &&
				(this.mPlatform.equals(theDS.mPlatform)) &&
				(this.mData.equals(theDS.mData)) &&
				(this.mAlgorithm.equals(theDS.mAlgorithm)) &&
				(this.mDetail.equals(theDS.mDetail)))
		{
			same = true;
		}
		return same;
	}
	
	static public String getHeaders()
	{
		return "ID	Files	Source	Variant	Project	Sub-Project	Category	Platform	Data	Algorithm	Details	Version	URL";
	}
	
	private String getFilesAsString()
	{
		String ret = null;
		for (String file : mFiles)
		{
			if (null==ret)
			{
				ret = file;
			}
			else
			{
				ret = ret + "|" + file;
			}
		}
		return ret;
	}
	
	public String toString(String theBaseURL)
	{
		String url = null;
		if (theBaseURL.endsWith("/"))
		{
			url = theBaseURL + "download?id=" + mID;
		}
		else
		{
			url = theBaseURL + "/download?id=" + mID;
		}
		return mID + "\t" + getFilesAsString() + "\t" + mSource + "\t" + mVariant + "\t" + mProject + "\t" + 
				mSubproject + "\t" + mCategory + "\t" + mPlatform + "\t" + mData + "\t" + mAlgorithm + "\t" + 
				mDetail + "\t" + mVersion + "\t" + url;

	}
}
