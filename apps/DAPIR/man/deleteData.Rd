\name{deleteData}
\alias{deleteData}
\title{deleteData}
\description{Delete directories of UNLISTED data.}
\usage{
deleteData(theUnlistedVector, theDownloadDir = getwd())
}
%- maybe also 'usage' for other objects documented here.
\arguments{
  \item{theUnlistedVector}{The UNLISTED entry (vector of IDs) from the list returned by checkDownloadedDataStatus. See \code{\link{checkDownloadedDataStatus}} }
  \item{theDownloadDir}{A string giving the directory containing or that will contain downloaded datasets.}
}
\details{
Delete the uncompressed directories given in the vector.
ZIP archives are not removed.

See \code{\link{checkDownloadedDataStatus}} for example.
}
\value{No meaningful return value.}
