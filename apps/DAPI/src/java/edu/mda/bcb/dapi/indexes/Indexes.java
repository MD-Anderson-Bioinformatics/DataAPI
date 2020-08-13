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

import edu.mda.bcb.dapi.query.Dataset;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.ServletContext;

/**
 *
 * @author linux
 */
public class Indexes
{
	public boolean mIsBEV = false;
	public boolean mIsDSC = false;
	private TreeMap<String, Dataset> mIdToDataset = null;
	private TreeSet<String> mNewestIds = null;
	private TreeMap<String, String> mIdToPath = null;
	private TreeMap<String, ArrayList<String>> mFileToIDs = null;
	private TreeMap<String, ArrayList<String>> mSourceToIDs = null;
	private TreeMap<String, ArrayList<String>> mVariantToIDs = null;
	private TreeMap<String, ArrayList<String>> mProjectToIDs = null;
	private TreeMap<String, ArrayList<String>> mSubprojectToIDs = null;
	private TreeMap<String, ArrayList<String>> mCategoryToIDs = null;
	private TreeMap<String, ArrayList<String>> mPlatformToIDs = null;
	private TreeMap<String, ArrayList<String>> mDatatypeToIDs = null;
	private TreeMap<String, ArrayList<String>> mAlgorithmToIDs = null;
	private TreeMap<String, ArrayList<String>> mDetailToIDs = null;
	private TreeMap<String, ArrayList<String>> mVersionToIDs = null;
	// Overall-DSC	Overall-DSC-pvalue
	private TreeMap<String, ArrayList<String>> mOverallDSCpvalueToIDs = null;

	public Indexes()
	{
		mIdToDataset = new TreeMap<>();
		mNewestIds = new TreeSet<>();
		mIdToPath = new TreeMap<>();
		mFileToIDs = new TreeMap<>();
		mSourceToIDs = new TreeMap<>();
		mVariantToIDs = new TreeMap<>();
		mProjectToIDs = new TreeMap<>();
		mSubprojectToIDs = new TreeMap<>();
		mCategoryToIDs = new TreeMap<>();
		mPlatformToIDs = new TreeMap<>();
		mDatatypeToIDs = new TreeMap<>();
		mAlgorithmToIDs = new TreeMap<>();
		mDetailToIDs = new TreeMap<>();
		mVersionToIDs = new TreeMap<>();
		mOverallDSCpvalueToIDs = new TreeMap<>();
	}
	
	synchronized public File getPath(String theId)
	{
		return new File(mIdToPath.get(theId));
	}
	
	public String roundToThreeDigits(String theString)
	{
		String retStr = theString;
		try
		{
			double myDbl = Double.parseDouble(theString);
			DecimalFormat df = new DecimalFormat("0.000");
			df.setRoundingMode(RoundingMode.HALF_UP);
			retStr = df.format(myDbl);
		}
		catch(Exception ignore)
		{
			// ignore errors, and keep original string
		}
		return retStr;
	}
	
	synchronized public void loadIndex(File theIndexFile, String theIndexSource,
			TreeMap<String, String> theBaseToCategory, TreeMap<String, String> theBaseToPlatform, TreeMap<String, String> theBaseToDetails) throws IOException, Exception
	{
		ArrayList<String> headersRqrd = getHeaders();
		ArrayList<String> headersOptn = getHeadersOptional();
		TreeSet<Dataset> datasets = new TreeSet<>();
		try (BufferedReader br = java.nio.file.Files.newBufferedReader(theIndexFile.toPath(), Charset.availableCharsets().get("UTF-8")))
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
						if (headersRqrd.contains(hdr))
						{
							headersIndex.add(hdr);
						}
						else if (headersOptn.contains(hdr))
						{
							headersIndex.add(hdr);
						}
						else 
						{
							throw new Exception("Unrecognized header '" + hdr + "' found in " + theIndexFile.getAbsolutePath());
						}
					}
				}
				else
				{
					String [] splitted = line.split("\t", -1);
					String lineIndex = (populateValue(splitted, headersIndex, "Variant") + " " + 
										populateValue(splitted, headersIndex, "Category") + " " + 
										populateValue(splitted, headersIndex, "Platform") + " " + 
										populateValue(splitted, headersIndex, "Details")).toLowerCase();
					// optional values
					String algo = populateValue(splitted, headersIndex, "Algo");
					String lvl1 = populateValue(splitted, headersIndex, "Lvl1");
					String lvl2 = populateValue(splitted, headersIndex, "Lvl2");
					// required values
					String newId = populateId(splitted, headersIndex, theIndexSource, "ID", "Path", algo, lvl1, lvl2);
					ArrayList<String> files = populateSplittedMap(newId, splitted, headersIndex, "Files", mFileToIDs, "\\|");
					String source = populateMap(newId, splitted, headersIndex, "Source", mSourceToIDs, null, null);
					String variant = populateMap(newId, splitted, headersIndex, "Variant", mVariantToIDs, null, null);
					String project = populateMap(newId, splitted, headersIndex, "Project", mProjectToIDs, null, null);
					String subproject = populateMap(newId, splitted, headersIndex, "Sub-Project", mSubprojectToIDs, null, null);
					String category = populateMap(newId, splitted, headersIndex, "Category", mCategoryToIDs, lineIndex, theBaseToCategory);
					String platform = populateMap(newId, splitted, headersIndex, "Platform", mPlatformToIDs, lineIndex, theBaseToPlatform);
					String data = populateMap(newId, splitted, headersIndex, "Data", mDatatypeToIDs, null, null);
					String algorithm = populateMap(newId, splitted, headersIndex, "Algorithm", mAlgorithmToIDs, null, null);
					String detail = populateMap(newId, splitted, headersIndex, "Details", mDetailToIDs, lineIndex, theBaseToDetails);
					String version = populateMap(newId, splitted, headersIndex, "Version", mVersionToIDs, null, null);
					// optional values
					String overallDSC_orig = populateValue(splitted, headersIndex, "Overall-DSC");
					overallDSC_orig = roundToThreeDigits(overallDSC_orig);
					Double overallDSC_dbl = null;
					if (null!=overallDSC_orig)
					{
						overallDSC_dbl = Double.parseDouble(overallDSC_orig);
					}
					String overallDSCpvalue_orig = populateValue(splitted, headersIndex, "Overall-DSC-pvalue");
					if ("5e-04".equals(overallDSCpvalue_orig))
					{
						overallDSCpvalue_orig = "0.0005";
					}
					String overallDSCpvalue_range = convertToDSCpvalueRange(overallDSCpvalue_orig);
					if (null!=overallDSCpvalue_range)
					{
						addToPopulateMapForDSCpvalue(newId, overallDSCpvalue_range, mOverallDSCpvalueToIDs);
					}
					// make dataset
					long dsSize1 = datasets.size();
					Dataset ds = new Dataset(theIndexSource, newId, files, source, variant, project, subproject, category, platform, data, algorithm, detail, version,
						algo, lvl1, lvl2, overallDSCpvalue_range, overallDSCpvalue_orig, overallDSC_dbl, overallDSC_orig);
					datasets.add(ds);
					long dsSize2 = datasets.size();
					if (dsSize2<=dsSize1)
					{
						System.err.println("Size did not increase");
					}
				}
				line = br.readLine();
			}
		}
		// copy datasets to lists
		Dataset oldDs = null;
		for(Dataset newDs : datasets)
		{
			mIdToDataset.put(newDs.mID, newDs);
			if ((null!=oldDs)&&(false==newDs.isSameExceptForVersion(oldDs)))
			{
				mNewestIds.add(oldDs.mID);
			}
			oldDs = newDs;
		}
		if (null!=oldDs)
		{
			mNewestIds.add(oldDs.mID);
		}
	}

	protected String convertToDSC_min(String theOrig)
	{
		// DSC Value
		// NA
		// Unknown
		// >0.900
		// >0.800
		// >0.700
		// ...
		// >0.100
		// >0.000
		String newValue = null;
		if (null!=theOrig)
		{
			float val = Float.valueOf(theOrig);
			if (val>0.900)
			{
				newValue = "0.900";
			}
			else if (val>0.800)
			{
				newValue = "0.800";
			}
			else if (val>0.700)
			{
				newValue = "0.700";
			}
			else if (val>0.600)
			{
				newValue = "0.600";
			}
			else if (val>0.500)
			{
				newValue = "0.500";
			}
			else if (val>0.400)
			{
				newValue = "0.400";
			}
			else if (val>0.300)
			{
				newValue = "0.300";
			}
			else if (val>0.200)
			{
				newValue = "0.200";
			}
			else if (val>0.100)
			{
				newValue = "0.100";
			}
			else if (val>0.000)
			{
				newValue = "0.000";
			}
			else 
			{
				newValue = "Unknown";
			}
		}
		return newValue;
	}
	
	protected String convertToDSCpvalueRange(String theOrig)
	{
		// P-Value
		// NA
		// Unknown
		// <1.0000
		// <0.5000
		// <0.1000
		// <0.0500
		// <0.0100
		// <0.0050
		// <0.0010
		// <0.0005
		String newValue = null;
		if (null!=theOrig)
		{
			if ("<0.0005".equals(theOrig))
			{
				newValue = "<0.0005";
			}
			else
			{
				float val = Float.valueOf(theOrig);
				if (val<0.0005)
				{
					newValue = "<0.0005";
				}
				else if (val<0.0010)
				{
					newValue = "<0.0010";
				}
				else if (val<0.0050)
				{
					newValue = "<0.0050";
				}
				else if (val<0.0100)
				{
					newValue = "<0.0100";
				}
				else if (val<0.0500)
				{
					newValue = "<0.0500";
				}
				else if (val<0.1000)
				{
					newValue = "<0.1000";
				}
				else if (val<0.5000)
				{
					newValue = "<0.5000";
				}
				else 
				{
					newValue = "Unknown";
				}
			}
		}
		return newValue;
	}
	
	protected String populateValue(String [] theSplitted, ArrayList<String> theHeadersIndex, String theHeader) throws Exception
	{
		String myValue = null;
		int index = theHeadersIndex.indexOf(theHeader);
		if (index>=0)
		{
			myValue = theSplitted[index];
		}
		return myValue;
	}
	
	protected String populateId(String [] theSplitted, ArrayList<String> theHeadersIndex, String theIndexSource, String theIdHeader, String thePathHeader,
			String theAlgo, String theLvl1, String theLvl2) throws Exception
	{
		String origId = theSplitted[theHeadersIndex.indexOf(theIdHeader)];
		String path = theSplitted[theHeadersIndex.indexOf(thePathHeader)];
		String id = theIndexSource + "-" + origId;
		if (null!=theAlgo)
		{
			id = id + "-" + theAlgo + "-" + theLvl1 + "-" + theLvl2;
		}
		String replace = mIdToPath.put(id, path);
		if (null!=replace)
		{
			throw new Exception("populateId found duplicate value for id=" + id);
		}
		return id;
	}
	
	protected void addToPopulateMapDSCmin(String theNewId, String theValue, TreeMap<String, ArrayList<String>> theMap)
	{
		if ("0.000".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
		}
		else if ("0.100".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
		}
		else if ("0.200".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
		}
		else if ("0.300".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
		}
		else if ("0.400".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
		}
		else if ("0.500".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
		}
		else if ("0.600".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
		}
		else if ("0.700".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
		}
		else if ("0.800".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
		}
		else if ("0.900".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.000", theMap);
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
		}
		else 
		{
			addToPopulateMap(theNewId, theValue, theMap);
		}
	}
	
	protected void addToPopulateMapDSCmax(String theNewId, String theValue, TreeMap<String, ArrayList<String>> theMap)
	{
		if ("0.100".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.100", theMap);
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.200".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.200", theMap);
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.300".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.300", theMap);
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.400".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.400", theMap);
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.500".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.500", theMap);
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.600".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.600", theMap);
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.700".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.700", theMap);
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.800".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.800", theMap);
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("0.900".equals(theValue))
		{
			addToPopulateMap(theNewId, "0.900", theMap);
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else if ("1.000".equals(theValue))
		{
			addToPopulateMap(theNewId, "1.000", theMap);
		}
		else
		{
			addToPopulateMap(theNewId, theValue, theMap);
		}
	}
	
	protected void addToPopulateMapForDSCpvalue(String theNewId, String theValue, TreeMap<String, ArrayList<String>> theMap)
	{
		if ("<0.0005".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
			addToPopulateMap(theNewId, "<0.5000", theMap);
			addToPopulateMap(theNewId, "<0.1000", theMap);
			addToPopulateMap(theNewId, "<0.0500", theMap);
			addToPopulateMap(theNewId, "<0.0100", theMap);
			addToPopulateMap(theNewId, "<0.0050", theMap);
			addToPopulateMap(theNewId, "<0.0010", theMap);
			addToPopulateMap(theNewId, "<0.0005", theMap);
		}
		else if ("<0.0010".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
			addToPopulateMap(theNewId, "<0.5000", theMap);
			addToPopulateMap(theNewId, "<0.1000", theMap);
			addToPopulateMap(theNewId, "<0.0500", theMap);
			addToPopulateMap(theNewId, "<0.0100", theMap);
			addToPopulateMap(theNewId, "<0.0050", theMap);
			addToPopulateMap(theNewId, "<0.0010", theMap);
		}
		else if ("<0.0050".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
			addToPopulateMap(theNewId, "<0.5000", theMap);
			addToPopulateMap(theNewId, "<0.1000", theMap);
			addToPopulateMap(theNewId, "<0.0500", theMap);
			addToPopulateMap(theNewId, "<0.0100", theMap);
			addToPopulateMap(theNewId, "<0.0050", theMap);
		}
		else if ("<0.0100".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
			addToPopulateMap(theNewId, "<0.5000", theMap);
			addToPopulateMap(theNewId, "<0.1000", theMap);
			addToPopulateMap(theNewId, "<0.0500", theMap);
			addToPopulateMap(theNewId, "<0.0100", theMap);
		}
		else if ("<0.0500".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
			addToPopulateMap(theNewId, "<0.5000", theMap);
			addToPopulateMap(theNewId, "<0.1000", theMap);
			addToPopulateMap(theNewId, "<0.0500", theMap);
		}
		else if ("<0.1000".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
			addToPopulateMap(theNewId, "<0.5000", theMap);
			addToPopulateMap(theNewId, "<0.1000", theMap);
		}
		else if ("<0.5000".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
			addToPopulateMap(theNewId, "<0.5000", theMap);
		}
		else if ("<1.0000".equals(theValue))
		{
			addToPopulateMap(theNewId, "<1.0000", theMap);
		}
		else
		{
			addToPopulateMap(theNewId, theValue, theMap);
		}
	}
	
	protected void addToPopulateMap(String theNewId, String theKey, TreeMap<String, ArrayList<String>> theMap)
	{
		ArrayList<String> myList = theMap.get(theKey);
		if (null==myList)
		{
			myList = new ArrayList<>();
		}
		myList.add(theNewId);
		theMap.put(theKey, myList);
	}

	protected String populateMap(String theNewId, String [] theSplitted, ArrayList<String> theHeadersIndex, String theHeader, TreeMap<String, ArrayList<String>> theMap,
			String theLineIndex, TreeMap<String, String> theNewValue)
	{
		String myValue = null;
		int index = theHeadersIndex.indexOf(theHeader);
		if (index>=0)
		{
			myValue = theSplitted[index];
			if ((null!=theLineIndex)&&(null!=theNewValue))
			{
				String newVal = theNewValue.get(theLineIndex);
				if (null!=newVal)
				{
					myValue = newVal;
				}
			}
			if (!"".equals(myValue))
			{
				addToPopulateMap(theNewId, myValue, theMap);
			}
		}
		return myValue;
	}

	protected ArrayList<String> populateSplittedMap(String theNewId, String [] theSplitted, ArrayList<String> theHeadersIndex, String theHeader, 
			TreeMap<String, ArrayList<String>> theMap, String theTokenizer)
	{
		ArrayList<String> dsVal = new ArrayList<>();
		String myValueList = theSplitted[theHeadersIndex.indexOf(theHeader)];
		String [] splitted = myValueList.split(theTokenizer, -1);
		for (String myValue : splitted)
		{
			dsVal.add(myValue);
			ArrayList<String> myList = theMap.get(myValue);
			if (null==myList)
			{
				myList = new ArrayList<>();
			}
			myList.add(theNewId);
			theMap.put(myValue, myList);
		}
		return dsVal;
	}
	
	static private ArrayList<String> getHeadersOptional()
	{
		ArrayList<String> headers = new ArrayList<>();
		headers.add("Algo");
		headers.add("Lvl1");
		headers.add("Lvl2");
		headers.add("Overall-DSC");
		headers.add("Overall-DSC-pvalue");
		headers.add("DSC(1,2)");
		headers.add("DSC-pvalue(1,2)");
		headers.add("DSC(1,3)");
		headers.add("DSC-pvalue(1,3)");
		headers.add("DSC(1,4)");
		headers.add("DSC-pvalue(1,4)");
		headers.add("DSC(2,3)");
		headers.add("DSC-pvalue(2,3)");
		headers.add("DSC(2,4)");
		headers.add("DSC-pvalue(2,4)");
		headers.add("DSC(3,4)");
		headers.add("DSC-pvalue(3,4)");
		return headers;
	}
	
	static private ArrayList<String> getHeaders()
	{
		ArrayList<String> headers = new ArrayList<>();
		headers.add("Path");
		headers.add("ID");
		// external
		headers.add("Files");
		headers.add("Source");
		headers.add("Variant");
		headers.add("Project");
		headers.add("Sub-Project");
		headers.add("Category");
		headers.add("Platform");
		headers.add("Data"); // dataset first half: Standardized, Analyzed, AutoCorrected, or Corrected
		headers.add("Algorithm"); // dataset second half: Discrete, Continuous, or the correction algorithm
		headers.add("Details"); // underscore specialization
		headers.add("Version");
		return headers;
	}
	
	synchronized static public Indexes loadFromFiles(String theDir, ServletContext theSC,
			TreeMap<String, String> theBaseToCategory, TreeMap<String, String> theBaseToPlatform, TreeMap<String, String> theBaseToDetails)
	{
		if (null!=theSC)
		{
			theSC.log("Indexes::loadFromFiles Load From " + theDir);
		}
		else
		{
			System.out.println("Indexes::loadFromFiles Load From " + theDir);
		}
		File [] indexes = new File(theDir).listFiles();
		Indexes myIndexes = new Indexes();
		for (File ind : indexes)
		{
			String source = ind.getName().split("_", -1)[0];
			try
			{
				if (null!=theSC)
				{
					theSC.log("Indexes::loadFromFiles Load " + ind.getAbsolutePath());
				}
				else
				{
					System.out.println("Indexes::loadFromFiles Load " + ind.getAbsolutePath());
				}
				if (ind.getName().startsWith("bev"))
				{
					myIndexes.mIsBEV = true;
				}
				if (ind.getName().startsWith("dsc"))
				{
					myIndexes.mIsDSC = true;
				}
				myIndexes.loadIndex(ind, source, theBaseToCategory, theBaseToPlatform, theBaseToDetails);
				if (null!=theSC)
				{
					theSC.log("Indexes::loadFromFiles loaded " + ind.getAbsolutePath());
				}
				else
				{
					System.out.println("Indexes::loadFromFiles loaded " + ind.getAbsolutePath());
				}
			}
			catch(Exception exp)
			{
				if (null!=theSC)
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
		return myIndexes;
	}
	
	synchronized public Collection<Dataset> getDatasets()
	{
		return mIdToDataset.values();
	}
	
	synchronized public TreeSet<String> getNewestIds()
	{
		return mNewestIds;
	}
	
	synchronized public Set<String> getIds()
	{
		return mIdToPath.keySet();
	}

	synchronized public Set<String> getKeysFiles()
	{
		return mFileToIDs.keySet();
	}

	synchronized public Set<String> getKeysSource()
	{
		return mSourceToIDs.keySet();
	}

	synchronized public Set<String> getKeysVariant()
	{
		return mVariantToIDs.keySet();
	}

	synchronized public Set<String> getKeysProject()
	{
		return mProjectToIDs.keySet();
	}

	synchronized public Set<String> getKeysSubProject()
	{
		return mSubprojectToIDs.keySet();
	}

	synchronized public Set<String> getKeysCategory()
	{
		return mCategoryToIDs.keySet();
	}

	synchronized public Set<String> getKeysPlatform()
	{
		return mPlatformToIDs.keySet();
	}

	synchronized public Set<String> getKeysData()
	{
		return mDatatypeToIDs.keySet();
	}

	synchronized public Set<String> getKeysAlgorithm()
	{
		return mAlgorithmToIDs.keySet();
	}

	synchronized public Set<String> getKeysDetails()
	{
		return mDetailToIDs.keySet();
	}

	synchronized public Set<String> getKeysVersion()
	{
		return mVersionToIDs.keySet();
	}

	synchronized public Set<String> getKeysOverallDSCpvalue()
	{
		return mOverallDSCpvalueToIDs.keySet();
	}
	
	protected TreeSet<String> internalQueryOverallDsc(TreeSet<String> theIds, Double theOverallDSCmin, Double theOverallDSCmax)
	{
		TreeSet<String> idList = new TreeSet<>();
		for (String id : theIds)
		{
			Dataset ds = mIdToDataset.get(id);
			boolean maxNotNull = null!=theOverallDSCmax;
			boolean minNotNull = null!=theOverallDSCmin;
			boolean maxOK = true;
			boolean minOK = true;
			if (maxNotNull)
			{
				maxOK = ds.mOverallDSC_Dbl<=theOverallDSCmax;
			}
			if (minNotNull)
			{
				minOK = ds.mOverallDSC_Dbl>=theOverallDSCmin;
			}
			if (maxOK && minOK)
			{
				idList.add(id);
			}
		}
		return idList;
	}
	
	protected TreeSet<String> internalQuery(ArrayList<String> theValues, TreeMap<String, ArrayList<String>> theValueToIds)
	{
		TreeSet<String> idList = new TreeSet<>();
		// idList is empty if no filter selected. is passed to combineQueryResults
		// empty idList means do not filter the available ids in combineQueryResults
		if ((null!=theValues)&&(theValues.size()>0))
		{
			for (String val : theValues)
			{
				ArrayList<String> ids = theValueToIds.get(val);
				idList.addAll(ids);
			}
		}
		return idList;
	}
	
	protected void combineQueryResults(TreeSet<String> theIds, TreeSet<String> theNewIds)
	{
		// theIds is never empty. It starts with all possible values
		if (!theNewIds.isEmpty())
		{
			theIds.retainAll(theNewIds);
		}
	}
	
	synchronized public TreeSet<Dataset> query(ArrayList<String> theFiles, ArrayList<String> theSources, ArrayList<String> theVariants, 
			ArrayList<String> theProjects, ArrayList<String> theSubprojects, ArrayList<String> theCategories, 
			ArrayList<String> thePlatforms, ArrayList<String> theData, ArrayList<String> theAlgorithms, 
			ArrayList<String> theDetails, ArrayList<String> theVersions,
			Double theOverallDSCmin, Double theOverallDSCmax, ArrayList<String> theOverallDSCpvalue)
	{
		TreeSet<String> ids = new TreeSet<>();
		// if version is empty, keep newest of each dataset, otherwise process as normal
		if ((null!=theVersions)&&(theVersions.size()>0))
		{
			ids.addAll(mIdToDataset.keySet());
		}
		else
		{
			ids.addAll(mNewestIds);
		}
		//
		combineQueryResults(ids, internalQuery(theFiles, mFileToIDs));
		combineQueryResults(ids, internalQuery(theSources, mSourceToIDs));
		combineQueryResults(ids, internalQuery(theVariants, mVariantToIDs));
		combineQueryResults(ids, internalQuery(theProjects, mProjectToIDs));
		combineQueryResults(ids, internalQuery(theSubprojects, mSubprojectToIDs));
		combineQueryResults(ids, internalQuery(theCategories, mCategoryToIDs));
		combineQueryResults(ids, internalQuery(thePlatforms, mPlatformToIDs));
		combineQueryResults(ids, internalQuery(theData, mDatatypeToIDs));
		combineQueryResults(ids, internalQuery(theAlgorithms, mAlgorithmToIDs));
		combineQueryResults(ids, internalQuery(theDetails, mDetailToIDs));
		combineQueryResults(ids, internalQuery(theVersions, mVersionToIDs));
		// optional
		// for overall DSC min and max, use ids =, since it can actually remove all available datasets from consideration
		ids = internalQueryOverallDsc(ids, theOverallDSCmin, theOverallDSCmax);
		combineQueryResults(ids, internalQuery(theOverallDSCpvalue, mOverallDSCpvalueToIDs));
		// if version is empty, keep newest of each dataset, otherwise process as normal
		TreeSet<Dataset> ds = new TreeSet<>();
		for (String id : ids)
		{
			ds.add(mIdToDataset.get(id));
		}
		return ds;
	}
}
