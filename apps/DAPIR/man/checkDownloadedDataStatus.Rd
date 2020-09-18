\name{checkDownloadedDataStatus}
\alias{checkDownloadedDataStatus}
\title{checkDownloadedDataStatus}
\description{Take a query string from the GUI and compare described datasets against downloaded versions and give results as NEW, UNLISTED, UNCHANGED.}
\usage{
checkDownloadedDataStatus(theQueryRequest, theDownloadDir)
}
\arguments{
  \item{theQueryRequest}{A string from the "Copy Query" clipboard in the Data Query Form for Standardized Data and Batch Effects.}
  \item{theDownloadDir}{A string giving the directory containing or that will contain downloaded datasets.}
}
\details{
Use the GUI to select data in which you are interested in. For example, this string gives current TCGA-ACC data from the 2020_01_31_0845 data run. Get the query request string by clicking the "Copy Query" button which puts the query on the clipboard. Use the query as a string (in double quotes) at theQueryRequest argument.

{\"mFiles\":[\"batches.tsv\"],\"mSources\":[],\"mVariants\":[\"current\"],\"mProjects\":[],\"mSubprojects\":[\"TCGA-ACC\"],\"mCategories\":[],\"mPlatforms\":[],\"mData\":[],\"mAlgorithms\":[],\"mDetails\":[],\"mVersions\":[\"2020_01_31_0845\"]}

The function returns a list with three names,"NEW", "UNLISTED", and "UNCHANGED" -- described under value below.
}
\value{
A list with three named elements: "NEW", "UNLISTED", and "UNCHANGED".
The NEW entry contains datasets that do not exist in theDownloadDir. The NEW element is a dataframe with columns ID, DESC, VERSION, and DOWNLOAD. The ID is the unique ID for that dataset in DAPI, the DESC is a textual description of the data, and VERSION is the version string. The DOWNLOAD entries are URLs for downloading the data.

The UNLISTED entry is a vector of dataset IDs which are in theDownloadDir but not reflected in the query string. This can be data that has been superseded or that is no longer available.

The UNCHANGED entry is a dataframe with the same columns as the NEW dataframe, listing datasets that are in the query but do not need to be updated.
}
\examples{
  # TCGA-ACC data
  queryOne <- paste("{\"mFiles\":[\"batches.tsv\"],\"mSources\":[],\"mVariants\":[\"current\"],",
                    "\"mProjects\":[],\"mSubprojects\":[\"TCGA-ACC\"],\"mCategories\":[],",
                    "\"mPlatforms\":[],\"mData\":[],\"mAlgorithms\":[],\"mDetails\":[],",
                    "\"mVersions\":[]}", sep="")
  # temp directory
  datasetDir <- file.path(tempdir(), "DAPIR")
  print(datasetDir)
  dir.create(datasetDir, showWarnings=FALSE, recursive=TRUE)
  # get data status
  results <- checkDownloadedDataStatus(queryOne, datasetDir)
  print(results)

  # Download initial datasets
  newDatasets <- results$NEW
  downloadData(newDatasets, datasetDir)

  # TCGA-ACC data from newest available versions
  queryTwo <- paste("{\"mFiles\":[\"batches.tsv\"],\"mSources\":[],\"mVariants\":[\"current\"],",
                    "\"mProjects\":[],\"mSubprojects\":[\"TCGA-ACC\"],\"mCategories\":[],",
                    "\"mPlatforms\":[],\"mData\":[],\"mAlgorithms\":[],\"mDetails\":[],",
                    "\"mVersions\":[]}", sep="")
  # get data status
  resultsTwo <- checkDownloadedDataStatus(queryTwo, datasetDir)
  print(resultsTwo)

  # remove old data
  unlistedVector <- resultsTwo$UNLISTED
  deleteData(unlistedVector, datasetDir)

}
