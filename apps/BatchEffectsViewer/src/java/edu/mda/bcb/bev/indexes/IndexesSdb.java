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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Tod-Casasent
 */
public class IndexesSdb extends Indexes
{
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

	public IndexesSdb()
	{
		super();
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
	}

	synchronized public File getPath(String theId)
	{
		return new File(mIdToPath.get(theId));
	}

	synchronized public void loadIndex(File theIndexFile, String theIndexSource) throws IOException, Exception
	{
		ArrayList<String> headersRqrd = getHeaders();
		TreeSet<Dataset> datasets = new TreeSet<>();
		try (BufferedReader br = java.nio.file.Files.newBufferedReader(theIndexFile.toPath(), Charset.availableCharsets().get("UTF-8")))
		{
			ArrayList<String> headersIndex = null;
			String line = br.readLine();
			while (null != line)
			{
				if (null == headersIndex)
				{
					headersIndex = new ArrayList<>();
					for (String hdr : line.split("\t", -1))
					{
						if (headersRqrd.contains(hdr))
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
					if (!"".equals(line.trim()))
					{
						String[] splitted = line.split("\t", -1);
						String newId = populateId(splitted, headersIndex, theIndexSource, "ID", "Path");
						ArrayList<String> files = populateSplittedMap(newId, splitted, headersIndex, "Files", mFileToIDs, "\\|");
						String source = populateMap(newId, splitted, headersIndex, "Source", mSourceToIDs);
						String variant = populateMap(newId, splitted, headersIndex, "Variant", mVariantToIDs);
						String project = populateMap(newId, splitted, headersIndex, "Project", mProjectToIDs);
						String subproject = populateMap(newId, splitted, headersIndex, "Sub-Project", mSubprojectToIDs);
						String category = populateMap(newId, splitted, headersIndex, "Category", mCategoryToIDs);
						String platform = populateMap(newId, splitted, headersIndex, "Platform", mPlatformToIDs);
						String data = populateMap(newId, splitted, headersIndex, "Data", mDatatypeToIDs);
						String algorithm = populateMap(newId, splitted, headersIndex, "Algorithm", mAlgorithmToIDs);
						String detail = populateMap(newId, splitted, headersIndex, "Details", mDetailToIDs);
						String version = populateMap(newId, splitted, headersIndex, "Version", mVersionToIDs);
						String path = splitted[headersIndex.indexOf("Path")];
						Dataset ds = new Dataset(theIndexSource, newId, files, source, variant, project, subproject, category, platform, data, algorithm, detail, version, path);
						datasets.add(ds);
					}
				}
				line = br.readLine();
			}
		}
		// copy datasets to lists
		Dataset oldDs = null;
		for (Dataset newDs : datasets)
		{
			mIdToDataset.put(newDs.mID, newDs);
			if ((null != oldDs) && (false == newDs.isSameExceptForVersion(oldDs)))
			{
				mNewestIds.add(oldDs.mID);
			}
			oldDs = newDs;
		}
		mNewestIds.add(oldDs.mID);
	}

	protected String populateId(String[] theSplitted, ArrayList<String> theHeadersIndex, String theIndexSource, String theIdHeader, String thePathHeader) throws Exception
	{
		String origId = theSplitted[theHeadersIndex.indexOf(theIdHeader)];
		String path = theSplitted[theHeadersIndex.indexOf(thePathHeader)];
		String id = theIndexSource + "-" + origId;
		String replace = mIdToPath.put(id, path);
		if (null != replace)
		{
			throw new Exception("populateId found duplicate value for id=" + id);
		}
		return id;
	}

	protected String populateMap(String theNewId, String[] theSplitted, ArrayList<String> theHeadersIndex, String theHeader, TreeMap<String, ArrayList<String>> theMap)
	{
		String myValue = theSplitted[theHeadersIndex.indexOf(theHeader)];
		if (!"".equals(myValue))
		{
			ArrayList<String> myList = theMap.get(myValue);
			if (null == myList)
			{
				myList = new ArrayList<>();
			}
			myList.add(theNewId);
			theMap.put(myValue, myList);
		}
		return myValue;
	}

	protected ArrayList<String> populateSplittedMap(String theNewId, String[] theSplitted, ArrayList<String> theHeadersIndex, String theHeader,
			TreeMap<String, ArrayList<String>> theMap, String theTokenizer)
	{
		ArrayList<String> dsVal = new ArrayList<>();
		String myValueList = theSplitted[theHeadersIndex.indexOf(theHeader)];
		String[] splitted = myValueList.split(theTokenizer, -1);
		for (String myValue : splitted)
		{
			dsVal.add(myValue);
			ArrayList<String> myList = theMap.get(myValue);
			if (null == myList)
			{
				myList = new ArrayList<>();
			}
			myList.add(theNewId);
			theMap.put(myValue, myList);
		}
		return dsVal;
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

	@Override
	synchronized public Dataset getDataset(String theId)
	{
		return mIdToDataset.get(theId);
	}

	@Override
	public File getZipPath(String theId)
	{
		return new File(mIdToDataset.get(theId).mPath);
	}

	@Override
	synchronized public Collection<Dataset> getDatasets()
	{
		return mIdToDataset.values();
	}

	@Override
	synchronized public TreeSet<String> getNewestIds()
	{
		return mNewestIds;
	}

	@Override
	synchronized public Set<String> getIds()
	{
		return mIdToPath.keySet();
	}

	@Override
	synchronized public Set<String> getKeysFiles()
	{
		return mFileToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysSource()
	{
		return mSourceToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysVariant()
	{
		return mVariantToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysProject()
	{
		return mProjectToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysSubProject()
	{
		return mSubprojectToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysCategory()
	{
		return mCategoryToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysPlatform()
	{
		return mPlatformToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysData()
	{
		return mDatatypeToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysAlgorithm()
	{
		return mAlgorithmToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysDetails()
	{
		return mDetailToIDs.keySet();
	}

	@Override
	synchronized public Set<String> getKeysVersion()
	{
		return mVersionToIDs.keySet();
	}

	protected TreeSet<String> internalQuery(ArrayList<String> theValues, TreeMap<String, ArrayList<String>> theValueToIds)
	{
		TreeSet<String> idList = new TreeSet<>();
		if ((null != theValues) && (theValues.size() > 0))
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
			ArrayList<String> theDetails, ArrayList<String> theVersions)
	{
		TreeSet<String> ids = new TreeSet<>();
		// if version is empty, keep newest of each dataset, otherwise process as normal
		if ((null != theVersions) && (theVersions.size() > 0))
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
		// if version is empty, keep newest of each dataset, otherwise process as normal
		TreeSet<Dataset> ds = new TreeSet<>();
		for (String id : ids)
		{
			ds.add(mIdToDataset.get(id));
		}
		return ds;
	}
}
