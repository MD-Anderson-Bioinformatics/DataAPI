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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
	//
	public String mAlgo;
	public String mLvl1;
	public String mLvl2;
	public String mOverallDSCpvalue;
	public String mOverallDSCpvalue_Orig;
	public Double mOverallDSC_Dbl;
	public String mOverallDSC_Orig;
							
	public Dataset(String theIndexSource, String mID, ArrayList<String> mFiles, String mSource, String mVariant, String mProject, String mSubproject, 
			String mCategory, String mPlatform, String mData, String mAlgorithm, String mDetail, String mVersion, 
			String theAlgo, String theLvl1, String theLvl2, 
			String theOverallDSCpvalue, String theOverallDSCpvalue_Orig, Double theOverallDSC_Dbl, String theOverallDSC_Orig)
	{
		this.mIndexSource = theIndexSource;
		this.mID = mID;
		this.mFiles = mFiles;
		this.mSource = mSource;
		this.mVariant = mVariant;
		this.mProject = mProject;
		this.mSubproject = mSubproject;
		this.mCategory = mCategory;
		this.mPlatform = mPlatform;
		this.mData = mData;
		this.mAlgorithm = mAlgorithm;
		this.mDetail = mDetail;
		this.mVersion = mVersion;
		// optional entries
		this.mAlgo = theAlgo;
		this.mLvl1 = theLvl1;
		this.mLvl2 = theLvl2;
		mOverallDSCpvalue = theOverallDSCpvalue;
		mOverallDSCpvalue_Orig = theOverallDSCpvalue_Orig;
		mOverallDSC_Dbl = theOverallDSC_Dbl;
		mOverallDSC_Orig = theOverallDSC_Orig;
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
											if (0==comp)
											{
												///////////////////
												// optional entries
												if (null != this.mAlgo)
												{
													comp = this.mAlgo.compareTo(o.mAlgo);
												}
												if (0==comp)
												{
													if (null != this.mLvl1)
													{
														comp = this.mLvl1.compareTo(o.mLvl1);
													}
													if (0==comp)
													{
														if (null != this.mLvl2)
														{
															comp = this.mLvl2.compareTo(o.mLvl2);
														}
														if (0==comp)
														{
															if (null != this.mOverallDSC_Orig)
															{
																comp = this.mOverallDSC_Orig.compareTo(o.mOverallDSC_Orig);
															}
															if (0==comp)
															{
																if (null != this.mOverallDSCpvalue_Orig)
																{
																	comp = this.mOverallDSCpvalue_Orig.compareTo(o.mOverallDSCpvalue_Orig);
																}
															}
														}
													}
												}
											}
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
				(this.mDetail.equals(theDS.mDetail)) &&
				(this.mAlgo.equals(theDS.mAlgo)) &&
				(this.mLvl1.equals(theDS.mLvl1)) &&
				(this.mLvl2.equals(theDS.mLvl2))
				)
		{
			same = true;
		}
		return same;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// headers
	////////////////////////////////////////////////////////////////////////////

	private ArrayList<String> headers(boolean isMbatch)
	{
		ArrayList<String> list = new ArrayList<>();
		list.add("ID");
		list.add("Files");
		list.add("Source");
		list.add("Variant");
		list.add("Project");
		list.add("Sub-Project");
		list.add("Category");
		list.add("Platform");
		list.add("Data");
		list.add("Algorithm");
		list.add("Details");
		list.add("Version");
		getHeadersOptional(list);
		if (isMbatch)
		{
			list.add("Download");
			list.add("View");
		}
		else
		{
			list.add("Download");
		}
		return list;
	}
	
	private void getHeadersOptional(ArrayList<String> theList)
	{
		if (null!=mAlgo)
		{
			theList.add("Algo");
			theList.add("Lvl1");
			theList.add("Lvl2");
			theList.add("Overall DSC pvalue");
			theList.add("Overall DSC");
		}
	}
	
	public String getHeaders(boolean isMbatch)
	{
		String headers = null;
		for (String col : headers(isMbatch))
		{
			if (null==headers)
			{
				headers = col;
			}
			else
			{
				headers = headers + "\t" + col;
			}
		}
		return headers;
	}
	
	private String columnValues(String theCol)
	{
		String res = "";
		if (	("ID".equals(theCol)) ||
				("Files".equals(theCol))
				)
		{
			res = ", \"visible\": false";
		}
		else if ("Download".equals(theCol))
		{
			// , "render":
			res = ", \"render\": \"downloadUrl\"";
			// \'function(data,type,row,meta){ return \"<a href='\" + data + \"'>Download Data</a>\"; }\'";
		}
		else if ("View".equals(theCol))
		{
			// , "render":function(data,type,row,meta){
			res = ", \"render\": \"viewUrl\"";
			// \'function(data,type,row,meta){ return \"<a target='_blank' href='\" + data + \"'>View Analysis</a>\"; }\'";
		}
		return res;
	}
	
	public String getJsonHeaders(boolean isMbatch)
	{
		String headers = null;
		for (String col : headers(isMbatch))
		{
			if (null==headers)
			{
				headers = "[" + "\n { \"title\": \"" + col + "\"" + columnValues(col) + " }";
			}
			else
			{
				headers = headers + ",\n { \"title\": \"" + col + "\"" + columnValues(col) + " }";
			}
		}
		if (null!=headers)
		{
			headers = headers + "\n]";
		}
		return headers;
	}

	////////////////////////////////////////////////////////////////////////////
	// dataset entry
	////////////////////////////////////////////////////////////////////////////
	
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
	
	private void toStringOptional(ArrayList<String> theList)
	{
		if (null!=mAlgo)
		{
			theList.add(mAlgo);
			theList.add(mLvl1);
			theList.add(mLvl2);
			theList.add(mOverallDSCpvalue_Orig);
			theList.add(mOverallDSC_Orig);
		}
	}
	
	private ArrayList<String> getStrings(String theStdURL, String theBevURL) throws UnsupportedEncodingException
	{
		ArrayList<String> list = new ArrayList<>();
		String stdURL = null;
		String bevUrl = null;
		if (theStdURL.endsWith("/"))
		{
			stdURL = theStdURL + "download?id=" + mID;
		}
		else
		{
			stdURL = theStdURL + "/download?id=" + mID;
		}
		if (null!=theBevURL)
		{
			if (mID.startsWith("dsc"))
			{
				
				bevUrl = theBevURL + "?id=" + mID.replace("dsc-", "bev-").replace("-"+mAlgo, "").replace("-"+mLvl1, "").replace("-"+mLvl2, "")
						+ "&index=" + mIndexSource.replace("dsc-", "bev-")
						+ "&alg=" + URLEncoder.encode("PCA+", StandardCharsets.UTF_8.name()) 
						+ "&lvl1=" + URLEncoder.encode(mLvl1, StandardCharsets.UTF_8.name());
			}
			else
			{
				bevUrl = theBevURL + "?id=" + mID  + "&index=" + mIndexSource;
			}
		}
		list.add(mID);
		list.add(getFilesAsString());
		list.add(mSource);
		list.add(mVariant);
		list.add(mProject);
		list.add(mSubproject);
		list.add(mCategory);
		list.add(mPlatform);
		list.add(mData);
		list.add(mAlgorithm);
		list.add(mDetail);
		list.add(mVersion);
		toStringOptional(list);
		list.add(stdURL);
		if (null!=theBevURL)
		{
			list.add(bevUrl);
		}
		return list;
	}
	
	public String toString(String theStdURL, String theBevURL) throws UnsupportedEncodingException
	{
		String myString = null;
		for (String val : getStrings(theStdURL, theBevURL))
		{
			if (null==myString)
			{
				myString = val;
			}
			else
			{
				myString = myString + "\t" + val;
			}
		}
		return myString;
	}

	public String getJson(Gson theBuilder, String theStdURL, String theBevURL) throws UnsupportedEncodingException
	{
		return theBuilder.toJson(getStrings(theStdURL, theBevURL));
	}
}
