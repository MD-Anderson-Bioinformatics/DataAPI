\name{downloadData}
\alias{downloadData}
\title{downloadData}
\description{Download the data described in the dataframe to the given directory.}
\usage{
downloadData(theNewDF, theDownloadDir = getwd())
}
\arguments{
  \item{theNewDF}{The NEW entry from the list returned by checkDownloadedDataStatus. See \code{\link{checkDownloadedDataStatus}} }
  \item{theDownloadDir}{A string giving the directory containing or that will contain downloaded datasets.}
}
\details{
Download and unzip the datasets listed in theNewDF in theDownloadDir. The ZIP files can be manually removed if desired.

See \code{\link{checkDownloadedDataStatus}} for example.
}
\value{No meaningful return value.}
